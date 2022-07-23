package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.preparser.param.ParamDefinitionManager;
import zly.rivulet.sql.preparser.SqlParamDefinitionManager;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

public class SkitDefinition extends AbstractDefinition {
    private ParamDefinition skit;

    public SkitDefinition(SqlPreParseHelper sqlPreParseHelper, Param<Integer> skit) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamDefinitionManager());
        ParamDefinitionManager paramDefinitionManager = sqlPreParseHelper.getParamDefinitionManager();
        this.skit = paramDefinitionManager.register(skit);
    }

    @Override
    public SkitDefinition forAnalyze() {
        return null;
    }
}
