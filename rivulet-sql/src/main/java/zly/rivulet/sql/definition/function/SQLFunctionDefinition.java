package zly.rivulet.sql.definition.function;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.sql.definition.SQLCustomDefinition;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.stream.Collectors;

public class SQLFunctionDefinition extends SQLCustomDefinition implements SingleValueElementDefinition {

    public SQLFunctionDefinition(SqlParserPortableToolbox toolbox, SQLFunction<?, ?> sqlFunction) {
        super(
            sqlFunction.getSingleValueList().stream()
            .map(toolbox::parseSingleValueForCondition)
            .collect(Collectors.toList()),
            sqlFunction.getCustomCollect()
        );

    }
}
