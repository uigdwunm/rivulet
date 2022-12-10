package zly.rivulet.sql.parser;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.StatementConvertor;
import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.StandardParam;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.definition.param.SQLParamReceipt;

public class SqlParamReceiptManager extends ParamReceiptManager {

    public SqlParamReceiptManager(ConvertorManager convertorManager) {
        super(convertorManager);
    }

    @Override
    protected ParamReceipt createParamDefinition(StandardParam<?> paramDesc, StatementConvertor<?> convertor) {
        return new SQLParamReceipt(paramDesc, convertor);
    }
}
