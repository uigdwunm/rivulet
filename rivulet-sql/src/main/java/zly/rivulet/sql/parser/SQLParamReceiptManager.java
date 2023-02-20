package zly.rivulet.sql.parser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.StatementConvertor;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.param.StandardParam;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.definition.param.SQLParamReceipt;

public class SQLParamReceiptManager extends ParamReceiptManager {

    public SQLParamReceiptManager(ConvertorManager convertorManager) {
        super(convertorManager);
    }

    @Override
    protected ParamReceipt createParamDefinition(StandardParam<?> paramDesc, StatementConvertor<?> convertor) {
        return new SQLParamReceipt(paramDesc, convertor);
    }
}
