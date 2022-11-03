package zly.rivulet.sql.definition.insert;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class ColumnItemDefinition extends AbstractDefinition {
    private final SQLFieldMeta sqlFieldMeta;

    private final ParamReceipt forModelMetaParamReceipt;

    protected ColumnItemDefinition(SqlParserPortableToolbox toolbox, SQLFieldMeta fieldMeta) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        this.forModelMetaParamReceipt = toolbox.getParamReceiptManager().registerParam(
            Param.of(
                fieldMeta.getFieldType(),
                fieldMeta.getFieldName(),
                SqlParamCheckType.NATURE
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
    public Definition forAnalyze() {
        return null;
    }
}
