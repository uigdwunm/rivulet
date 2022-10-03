package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class LimitDefinition extends AbstractDefinition {
    private final ParamReceipt limit;

    public LimitDefinition(SqlParserPortableToolbox sqlPreParseHelper, Param<Integer> limit) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamReceiptManager());
        ParamReceiptManager paramReceiptManager = sqlPreParseHelper.getParamReceiptManager();
        this.limit = paramReceiptManager.registerParam(limit);
    }

    @Override
    public LimitDefinition forAnalyze() {
        return null;
    }
}
