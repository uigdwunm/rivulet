package zly.rivulet.sql.definition.query_.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.parser.toolbox_.SQLParserPortableToolbox;

public class LimitDefinition extends AbstractDefinition {
    private final ParamReceipt limit;

    /**
     * 这里加上skit的部分，可能有的sql语法用的上
     **/
    private final SkitDefinition skit;

    private LimitDefinition(CheckCondition checkCondition, SkitDefinition skit, ParamReceipt limit) {
        super(checkCondition, null);
        this.limit = limit;
        this.skit = skit;
    }

    public LimitDefinition(SQLParserPortableToolbox sqlPreParseHelper, SkitDefinition skit, Param<Integer> limit) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamReceiptManager());
        ParamReceiptManager paramReceiptManager = sqlPreParseHelper.getParamReceiptManager();
        this.limit = paramReceiptManager.registerParam(limit);
        this.skit = skit;
    }

    public ParamReceipt getLimitParam() {
        return limit;
    }

    public SkitDefinition getSkit() {
        return skit;
    }

    @Override
    public Copier copier() {
        return new Copier(limit, skit);
    }

    public class Copier implements Definition.Copier {

        private ParamReceipt limit;

        private SkitDefinition skit;

        private Copier(ParamReceipt limit, SkitDefinition skit) {
            this.limit = limit;
            this.skit = skit;
        }

        public void setSkit(SkitDefinition skit) {
            this.skit = skit;
        }

        public void setLimit(ParamReceipt limit) {
            this.limit = limit;
        }

        @Override
        public LimitDefinition copy() {
            return new LimitDefinition(checkCondition, skit, limit);
        }
    }
}
