package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.parser.param.ParamDefinitionManager;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class SkitDefinition extends AbstractDefinition {
    private ParamDefinition skit;

    public SkitDefinition(SqlParserPortableToolbox sqlPreParseHelper, Param<Integer> skit) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamDefinitionManager());
        ParamDefinitionManager paramDefinitionManager = sqlPreParseHelper.getParamDefinitionManager();
        this.skit = paramDefinitionManager.registerParam(skit);
    }

    @Override
    public SkitDefinition forAnalyze() {
        return null;
    }
}
