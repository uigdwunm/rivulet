package zly.rivulet.base.definition.param;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.StatementConvertor;
import zly.rivulet.base.definition.Definition;

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
    private final StatementConvertor<Object> convertor;

    public PathKeyParamReceipt(String pathKey, Class<?> type, StatementConvertor<?> convertor) {
        this.pathKey = pathKey;
        this.type = type;
        this.convertor = (StatementConvertor<Object>) convertor;
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }

    @Override
    public StatementConvertor<Object> getConvertor() {
        return this.convertor;
    }

    public String getPathKey() {
        return pathKey;
    }

    @Override
    public Copier copier() {
        return new Copier(this);
    }

    public class Copier implements Definition.Copier {

        private final PathKeyParamReceipt pathKeyParamReceipt;

        public Copier(PathKeyParamReceipt pathKeyParamReceipt) {
            this.pathKeyParamReceipt = pathKeyParamReceipt;
        }

        @Override
        public PathKeyParamReceipt copy() {
            return pathKeyParamReceipt;
        }
    }
}
