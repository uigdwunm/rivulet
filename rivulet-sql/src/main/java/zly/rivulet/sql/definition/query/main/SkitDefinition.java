package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.parser.toolbox_.SQLParserPortableToolbox;

public class SkitDefinition extends AbstractDefinition {
    private ParamReceipt skit;

    private SkitDefinition(CheckCondition checkCondition, ParamReceipt skit) {
        super(checkCondition, null);
        this.skit = skit;
    }

    public SkitDefinition(SQLParserPortableToolbox sqlPreParseHelper, Param<Integer> skit) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getParamReceiptManager());
        ParamReceiptManager paramReceiptManager = sqlPreParseHelper.getParamReceiptManager();
        this.skit = paramReceiptManager.registerParam(skit);
    }

    public ParamReceipt getSkit() {
        return skit;
    }

    @Override
    public Copier copier() {
        return new Copier(skit);
    }

    public class Copier implements Definition.Copier {
        private ParamReceipt skit;

        private Copier(ParamReceipt skit) {
            this.skit = skit;
        }

        @Override
        public SkitDefinition copy() {
            return new SkitDefinition(checkCondition, skit);
        }
    }
}
