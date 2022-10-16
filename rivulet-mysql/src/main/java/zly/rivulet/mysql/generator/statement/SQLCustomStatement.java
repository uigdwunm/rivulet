package zly.rivulet.mysql.generator.statement;

import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.definition.SQLCustomDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class SQLCustomStatement implements SqlStatement {

    private final List<CustomSingleValueWrap> singleValueList;

    private final BiConsumer<CustomCollector, List<CustomSingleValueWrap>> customCollect;

    public SQLCustomStatement(
        List<CustomSingleValueWrap> singleValueList,
        BiConsumer<CustomCollector, List<CustomSingleValueWrap>> customCollect
    ) {
        this.singleValueList = singleValueList;
        this.customCollect = customCollect;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        customCollect.accept(new CustomCollector(collector), singleValueList);
    }

    public static class SQLCustomSingleValueWrap implements CustomSingleValueWrap {
        private final SingleValueElementStatement singleValueElementStatement;

        private SQLCustomSingleValueWrap(SingleValueElementStatement singleValueElementStatement) {
            this.singleValueElementStatement = singleValueElementStatement;
        }

        @Override
        public void collectSingleValue(StatementCollector collector) {
            singleValueElementStatement.singleCollectStatement(collector);
        }
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SQLCustomDefinition.class,
            (definition, soleFlag, initHelper) -> {
                SQLCustomDefinition sqlCustomDefinition = (SQLCustomDefinition) definition;
                List<CustomSingleValueWrap> singleValueList = sqlCustomDefinition.getSingleValueList().stream()
                    .map(singleValueElementDefinition -> {
                        SqlStatement sqlStatement = sqlStatementFactory.warmUp(singleValueElementDefinition, soleFlag.subSwitch(), initHelper);
                        return new SQLCustomSingleValueWrap((SingleValueElementStatement) sqlStatement);
                    }).collect(Collectors.toList());
                return new SQLCustomStatement(singleValueList, sqlCustomDefinition.getCustomCollect());
            },
            (definition, helper) -> {
                SQLCustomDefinition sqlCustomDefinition = (SQLCustomDefinition) definition;
                List<CustomSingleValueWrap> singleValueList = sqlCustomDefinition.getSingleValueList().stream()
                    .map(singleValueElementDefinition -> {
                        SqlStatement sqlStatement = sqlStatementFactory.getOrCreate(singleValueElementDefinition, helper);
                        return new SQLCustomSingleValueWrap((SingleValueElementStatement) sqlStatement);
                    }).collect(Collectors.toList());
                return new SQLCustomStatement(singleValueList, sqlCustomDefinition.getCustomCollect());
            }
        );
    }
}
