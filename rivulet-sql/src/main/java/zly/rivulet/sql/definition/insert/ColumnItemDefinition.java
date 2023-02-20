package zly.rivulet.sql.definition.insert;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.ParamCheckType;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.parser.toolbox.SQLParserPortableToolbox;

public class ColumnItemDefinition extends AbstractDefinition {
    private final SQLFieldMeta sqlFieldMeta;

    private final ParamReceipt forModelMetaParamReceipt;

    private ColumnItemDefinition(CheckCondition checkCondition, SQLFieldMeta sqlFieldMeta, ParamReceipt forModelMetaParamReceipt) {
        super(checkCondition, null);
        this.sqlFieldMeta = sqlFieldMeta;
        this.forModelMetaParamReceipt = forModelMetaParamReceipt;
    }

    protected ColumnItemDefinition(SQLParserPortableToolbox toolbox, SQLFieldMeta fieldMeta) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        this.forModelMetaParamReceipt = toolbox.getParamReceiptManager().registerParam(
            Param.of(
                fieldMeta.getFieldType(),
                fieldMeta.getFieldName(),
                ParamCheckType.NATURE
            )
        );
        this.sqlFieldMeta = fieldMeta;
    }

    public SQLFieldMeta getSqlFieldMeta() {
        return sqlFieldMeta;
    }

    public ParamReceipt getForModelMetaParamReceipt() {
        return forModelMetaParamReceipt;
    }

    @Override
    public Copier copier() {
        return new Copier(sqlFieldMeta, forModelMetaParamReceipt);
    }

    public class Copier implements Definition.Copier {

        private SQLFieldMeta sqlFieldMeta;

        private ParamReceipt forModelMetaParamReceipt;

        public Copier(SQLFieldMeta sqlFieldMeta, ParamReceipt forModelMetaParamReceipt) {
            this.sqlFieldMeta = sqlFieldMeta;
            this.forModelMetaParamReceipt = forModelMetaParamReceipt;
        }

        public void setSqlFieldMeta(SQLFieldMeta sqlFieldMeta) {
            this.sqlFieldMeta = sqlFieldMeta;
        }

        public void setForModelMetaParamReceipt(ParamReceipt forModelMetaParamReceipt) {
            this.forModelMetaParamReceipt = forModelMetaParamReceipt;
        }

        @Override
        public ColumnItemDefinition copy() {
            return new ColumnItemDefinition(checkCondition, sqlFieldMeta, forModelMetaParamReceipt);
        }
    }
}
