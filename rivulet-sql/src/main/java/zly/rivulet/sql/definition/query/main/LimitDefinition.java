package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.parser.param.ParamDefinitionManager;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class LimitDefinition extends AbstractDefinition {
    private final ParamDefinition limit;

    public LimitDefinition(SqlParserPortableToolbox sqlPreParseHelper, Param<Integer> limit) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamDefinitionManager());
        ParamDefinitionManager paramDefinitionManager = sqlPreParseHelper.getParamDefinitionManager();
        this.limit = paramDefinitionManager.registerParam(limit);
    }

    @Override
    public LimitDefinition forAnalyze() {
        return null;
    }
}
