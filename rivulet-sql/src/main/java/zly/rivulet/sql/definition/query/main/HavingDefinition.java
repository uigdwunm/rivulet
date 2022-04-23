package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.describer.query.desc.Condition;
import zly.rivulet.sql.preparser.SqlPreParseHelper;

import java.util.List;

public class HavingDefinition extends AbstractDefinition {
    protected HavingDefinition() {
        super(CheckCondition.IS_TRUE);
    }

    public HavingDefinition(SqlPreParseHelper sqlPreParseHelper, List<? extends Condition<?,?>> havingItemList) {
        this();
    }

    @Override
    public HavingDefinition forAnalyze() {
        return null;
    }
}
