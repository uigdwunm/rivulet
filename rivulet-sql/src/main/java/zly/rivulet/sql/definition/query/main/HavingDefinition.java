package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.List;

public class HavingDefinition extends AbstractDefinition {

    public HavingDefinition(SqlParserPortableToolbox sqlPreParseHelper, List<? extends Condition<?,?>> havingItemList) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamDefinitionManager());
    }

    @Override
    public HavingDefinition forAnalyze() {
        return null;
    }
}
