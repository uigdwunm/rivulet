package zly.rivulet.base.definition.param;

import zly.rivulet.base.convertor.Convertor;

public class StaticParamReceipt implements ParamReceipt {

    /**
     * Description 参数值
     *
     * @author zhaolaiyuan
     * Date 2022/8/29 8:16
     **/
    private final Object paramValue;

    /**
     * Description 参数类型
     *
     * @author zhaolaiyuan
     * Date 2022/8/29 8:22
     **/
    private final Class<?> type;

    /**
     * Description 对应的转换器，从参数转换成语句的string
     *
     * @author zhaolaiyuan
     * Date 2022/8/29 8:23
     **/
    private final Convertor<?, ?> convertor;

    public StaticParamReceipt(Object paramValue, Convertor<?, ?> convertor) {
        this.paramValue = paramValue;
        this.type = paramValue.getClass();
        this.convertor = convertor;
    }

    public Object getParamValue() {
        return this.paramValue;
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }

    @Override
    public Convertor<?, ?> getConvertor() {
        return this.convertor;
    }
}