package zly.rivulet.mysql.generator.statement;

import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;
import zly.rivulet.sql.definition.SQLCustomDefinition;
import zly.rivulet.sql.definition.function.SQLFunctionDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class SQLFunctionStatement extends SQLCustomStatement implements SingleValueElementStatement {

    public SQLFunctionStatement(List<CustomSingleValueWrap> singleValueList, BiConsumer<CustomCollector, List<CustomSingleValueWrap>> customCollect) {
        super(singleValueList, customCollect);
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        SQLStatementFactory.StatementInitCreator statementInitCreator = (definition, soleFlag, initHelper) -> {
            SQLCustomDefinition sqlCustomDefinition = (SQLCustomDefinition) definition;
            List<CustomSingleValueWrap> singleValueList = sqlCustomDefinition.getSingleValueList().stream()
                .map(singleValueElementDefinition -> {
                    SQLStatement sqlStatement = sqlStatementFactory.warmUp(singleValueElementDefinition, soleFlag.subSwitch(), initHelper);
                    return new SQLCustomSingleValueWrap((SingleValueElementStatement) sqlStatement);
                }).collect(Collectors.toList());
            return new SQLFunctionStatement(singleValueList, sqlCustomDefinition.getCustomCollect());
        };

        SQLStatementFactory.StatementRunCreator statementRunCreator = (definition, helper) -> {
            SQLCustomDefinition sqlCustomDefinition = (SQLCustomDefinition) definition;
            List<CustomSingleValueWrap> singleValueList = sqlCustomDefinition.getSingleValueList().stream()
                .map(singleValueElementDefinition -> {
                    SQLStatement sqlStatement = sqlStatementFactory.getOrCreate(singleValueElementDefinition, helper);
                    return new SQLCustomSingleValueWrap((SingleValueElementStatement) sqlStatement);
                }).collect(Collectors.toList());
            return new SQLFunctionStatement(singleValueList, sqlCustomDefinition.getCustomCollect());
        };
        sqlStatementFactory.register(SQLFunctionDefinition.class, statementInitCreator, statementRunCreator);
    }
}
