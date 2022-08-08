package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.describer.query.desc.OrderBy;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.List;

public class OrderByDefinition extends AbstractDefinition {

    public OrderByDefinition(SqlParserPortableToolbox sqlPreParseHelper, List<? extends OrderBy.Item<?,?>> orderFieldList) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamDefinitionManager());
    }

    @Override
    public OrderByDefinition forAnalyze() {
        return null;
    }
}
