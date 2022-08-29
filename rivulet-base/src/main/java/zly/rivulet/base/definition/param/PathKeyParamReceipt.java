package zly.rivulet.base.definition.param;

import zly.rivulet.base.convertor.Convertor;

public class PathKeyParamReceipt implements ParamReceipt {

    private final String pathKey;

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

    public PathKeyParamReceipt(String pathKey, Class<?> type, Convertor<?, ?> convertor) {
        this.pathKey = pathKey;
        this.type = type;
        this.convertor = convertor;
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }

    @Override
    public Convertor<?, ?> getConvertor() {
        return this.convertor;
    }

    public String getPathKey() {
        return pathKey;
    }
}
