package zly.rivulet.sql.definition.param;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definition.param.PathKeyParamReceipt;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.StandardParam;
import zly.rivulet.sql.describer.param.SqlParamCheckType;

public class SQLParamReceipt extends PathKeyParamReceipt {

    private final StandardParam<?> originDesc;

    // 拼接方式
    private final SqlParamCheckType sqlParamCheckType;

    public SQLParamReceipt(StandardParam<?> paramDesc, Convertor<?, ?> convertor) {
        super(paramDesc.getPathKey(), paramDesc.getParamType(), convertor);
        this.originDesc = paramDesc;
        if (paramDesc.getParamCheckType() != null) {
            this.sqlParamCheckType = (SqlParamCheckType) paramDesc.getParamCheckType();
        } else {
            this.sqlParamCheckType = SqlParamCheckType.PLACEHOLDER;
        }


    }

    public SqlParamCheckType getSqlParamCheckType() {
        return sqlParamCheckType;
    }

    public Param<?> getOriginDesc() {
        return this.originDesc;
    }

}
