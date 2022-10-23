package zly.rivulet.sql.definition.function;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.sql.definition.SQLCustomDefinition;
import zly.rivulet.sql.describer.function.Function;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.stream.Collectors;

public class SQLFunctionDefinition extends SQLCustomDefinition implements SingleValueElementDefinition {

    public SQLFunctionDefinition(SqlParserPortableToolbox toolbox, Function<?, ?> sqlFunction) {
        super(toolbox);
        super.customCollect = sqlFunction.getCustomCollect();
        super.singleValueList = sqlFunction.getSingleValueList().stream()
            .map(toolbox::parseSingleValueForCondition)
            .collect(Collectors.toList());

    }

    @Override
    public Definition forAnalyze() {
        return null;
    }
}
