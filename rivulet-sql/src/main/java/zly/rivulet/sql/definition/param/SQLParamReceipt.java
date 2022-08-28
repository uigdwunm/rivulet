package zly.rivulet.sql.definition.param;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definer.outerType.SelfType;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.param.SqlParamCheckType;

public class SQLParamReceipt implements ParamReceipt {

    private final Param<?> originDesc;

    // 拼接方式
    private final SqlParamCheckType sqlParamCheckType;

    // 参数类型
    private final Class<?> clazz;

    // 对应的转换器，从参数转换成语句的string
    private final Convertor<?, ?> convertor;

    public SQLParamReceipt(Param<?> paramDesc, FieldMeta fieldMeta, ConvertorManager convertorManager) {
        this.originDesc = paramDesc;
        if (fieldMeta == null) {
            this.convertor = convertorManager.get(paramDesc.getClazz(), SelfType.class);
        } else {
            this.convertor = convertorManager.get(paramDesc.getClazz(), fieldMeta.getOriginOuterType());
        }
        if (paramDesc.getParamCheckType() != null) {
            this.sqlParamCheckType = (SqlParamCheckType) paramDesc.getParamCheckType();
        } else {
            this.sqlParamCheckType = SqlParamCheckType.PLACEHOLDER;
        }
        this.clazz = paramDesc.getClazz();


    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    @Override
    public Convertor<?, ?> getConvertor() {
        return this.convertor;
    }

    public SqlParamCheckType getSqlParamCheckType() {
        return sqlParamCheckType;
    }

    @Override
    public Param<?> getOriginDesc() {
        return this.originDesc;
    }

}
