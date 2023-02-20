package zly.rivulet.mysql.generator.statement;

import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.definition.SQLCustomDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class SQLCustomStatement extends SQLStatement {

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
    public int length() {
        return 0;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        customCollect.accept(new CustomCollector(collector), singleValueList);
    }

    public static class SQLCustomSingleValueWrap implements CustomSingleValueWrap {
        private final SingleValueElementStatement singleValueElementStatement;

        protected SQLCustomSingleValueWrap(SingleValueElementStatement singleValueElementStatement) {
            this.singleValueElementStatement = singleValueElementStatement;
        }

        @Override
        public void collectSingleValue(StatementCollector collector) {
            singleValueElementStatement.collectStatementOrCache(collector);
        }
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        SQLStatementFactory.StatementInitCreator statementInitCreator = (definition, soleFlag, initHelper) -> {
            SQLCustomDefinition sqlCustomDefinition = (SQLCustomDefinition) definition;
            List<CustomSingleValueWrap> singleValueList = sqlCustomDefinition.getSingleValueList().stream()
                .map(singleValueElementDefinition -> {
                    SQLStatement sqlStatement = sqlStatementFactory.warmUp(singleValueElementDefinition, soleFlag.subSwitch(), initHelper);
                    return new SQLCustomSingleValueWrap((SingleValueElementStatement) sqlStatement);
                }).collect(Collectors.toList());
            return new SQLCustomStatement(singleValueList, sqlCustomDefinition.getCustomCollect());
        };

        SQLStatementFactory.StatementRunCreator statementRunCreator = (definition, helper) -> {
                SQLCustomDefinition sqlCustomDefinition = (SQLCustomDefinition) definition;
                List<CustomSingleValueWrap> singleValueList = sqlCustomDefinition.getSingleValueList().stream()
                    .map(singleValueElementDefinition -> {
                        SQLStatement sqlStatement = sqlStatementFactory.getOrCreate(singleValueElementDefinition, helper);
                        return new SQLCustomSingleValueWrap((SingleValueElementStatement) sqlStatement);
                    }).collect(Collectors.toList());
                return new SQLCustomStatement(singleValueList, sqlCustomDefinition.getCustomCollect());
            };
        sqlStatementFactory.register(SQLCustomDefinition.class, statementInitCreator, statementRunCreator);
    }
}
