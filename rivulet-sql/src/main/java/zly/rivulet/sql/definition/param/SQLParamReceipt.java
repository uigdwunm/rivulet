package zly.rivulet.sql.definition.param;

import zly.rivulet.base.convertor.StatementConvertor;
import zly.rivulet.base.definition.param.PathKeyParamReceipt;
import zly.rivulet.base.describer.param.ParamCheckType;
import zly.rivulet.base.describer.param.StandardParam;

public class SQLParamReceipt extends PathKeyParamReceipt {

    private final StandardParam<?> originDesc;

    // 拼接方式
    private final ParamCheckType sqlParamCheckType;

    public SQLParamReceipt(StandardParam<?> paramDesc, StatementConvertor<?> convertor) {
        super(paramDesc.getPathKey(), paramDesc.getParamType(), convertor);
        this.originDesc = paramDesc;
        if (paramDesc.getParamCheckType() != null) {
            this.sqlParamCheckType = (ParamCheckType) paramDesc.getParamCheckType();
        } else {
            this.sqlParamCheckType = ParamCheckType.PLACEHOLDER;
        }


    }

    public ParamCheckType getSqlParamCheckType() {
        return sqlParamCheckType;
    }

    public StandardParam<?> getOriginDesc() {
        return this.originDesc;
    }

}
