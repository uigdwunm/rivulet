package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.describer.query.desc.OrderBy;
import zly.rivulet.sql.preparser.SqlPreParseHelper;

import java.util.List;

public class OrderByDefinition extends AbstractDefinition {
    protected OrderByDefinition() {
        super(CheckCondition.IS_TRUE);
    }

    public OrderByDefinition(SqlPreParseHelper sqlPreParseHelper, List<? extends OrderBy.Item<?,?>> orderFieldList) {
        this();
    }

    @Override
    public OrderByDefinition forAnalyze() {
        return null;
    }
}
