package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.sql.describer.query.desc.AbstractCondition;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

public class BetweenOperateDefinition extends OperateDefinition {

    public BetweenOperateDefinition(SqlPreParseHelper sqlPreParseHelper, AbstractCondition<?, ?, ?> condition) {
        super(condition.getCheckCondition(), sqlPreParseHelper.getSqlParamDefinitionManager());

    }

    @Override
    public Definition forAnalyze() {
        return null;
    }
}
