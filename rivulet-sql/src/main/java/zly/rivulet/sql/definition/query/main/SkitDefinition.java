package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.preparser.SqlParamDefinitionManager;
import zly.rivulet.sql.preparser.SqlPreParseHelper;

public class SkitDefinition extends AbstractDefinition {
    private ParamDefinition skit;

    protected SkitDefinition() {
        super(CheckCondition.IS_TRUE);
    }

    public SkitDefinition(SqlPreParseHelper sqlPreParseHelper, Param<Integer> skit) {
        this();
        SqlParamDefinitionManager sqlParamDefinitionManager = sqlPreParseHelper.getSqlParamDefinitionManager();
        this.skit = sqlParamDefinitionManager.register(skit);
    }

    @Override
    public SkitDefinition forAnalyze() {
        SkitDefinition skitDefinition = new SkitDefinition();
        skitDefinition.skit = skit;
        return skitDefinition;
    }
}
