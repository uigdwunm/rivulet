package zly.rivulet.sql.parser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.definition.param.SQLParamReceipt;

public class SqlParamReceiptManager extends ParamReceiptManager {

    public SqlParamReceiptManager(ConvertorManager convertorManager) {
        super(convertorManager);
    }

    @Override
    protected ParamReceipt createParamDefinition(Param<?> paramDesc, FieldMeta fieldMeta) {
        return new SQLParamReceipt(paramDesc, fieldMeta, super.convertorManager);
    }
}
