package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class SkitDefinition extends AbstractDefinition {
    private ParamReceipt skit;

    public SkitDefinition(SqlParserPortableToolbox sqlPreParseHelper, Param<Integer> skit) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamDefinitionManager());
        ParamReceiptManager paramReceiptManager = sqlPreParseHelper.getParamDefinitionManager();
        this.skit = paramReceiptManager.registerParam(skit);
    }

    @Override
    public SkitDefinition forAnalyze() {
        return null;
    }
}
