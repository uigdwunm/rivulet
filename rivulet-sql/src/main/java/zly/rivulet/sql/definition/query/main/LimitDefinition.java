package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.preparser.SqlParamDefinitionManager;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

public class LimitDefinition extends AbstractDefinition {
    private final ParamDefinition limit;

    public LimitDefinition(SqlPreParseHelper sqlPreParseHelper, Param<Integer> limit) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getSqlParamDefinitionManager());
        SqlParamDefinitionManager sqlParamDefinitionManager = sqlPreParseHelper.getSqlParamDefinitionManager();
        this.limit = sqlParamDefinitionManager.register(limit);
    }

    @Override
    public LimitDefinition forAnalyze() {
        return null;
    }
}
