package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.describer.query.condition.Condition;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

import java.util.List;

public class HavingDefinition extends AbstractDefinition {

    public HavingDefinition(SqlPreParseHelper sqlPreParseHelper, List<? extends Condition<?,?>> havingItemList) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamDefinitionManager());
    }

    @Override
    public HavingDefinition forAnalyze() {
        return null;
    }
}
