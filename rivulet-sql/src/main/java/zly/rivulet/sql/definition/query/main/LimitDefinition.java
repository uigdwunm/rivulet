package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class LimitDefinition extends AbstractDefinition {
    private final ParamReceipt limit;

    private LimitDefinition(CheckCondition checkCondition, ParamReceipt limit) {
        super(checkCondition, null);
        this.limit = limit;
    }

    public LimitDefinition(SqlParserPortableToolbox sqlPreParseHelper, Param<Integer> limit) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamReceiptManager());
        ParamReceiptManager paramReceiptManager = sqlPreParseHelper.getParamReceiptManager();
        this.limit = paramReceiptManager.registerParam(limit);
    }

    @Override
    public Copier copier() {
        return new Copier(limit);
    }

    public class Copier implements Definition.Copier {

        private ParamReceipt limit;

        private Copier(ParamReceipt limit) {
            this.limit = limit;
        }

        public void setLimit(ParamReceipt limit) {
            this.limit = limit;
        }

        @Override
        public LimitDefinition copy() {
            return new LimitDefinition(checkCondition, limit);
        }
    }
}
