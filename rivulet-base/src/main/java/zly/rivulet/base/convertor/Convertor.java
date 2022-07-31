package zly.rivulet.base.convertor;

import zly.rivulet.base.definer.outerType.OriginOuterType;

public abstract class Convertor<T, O extends OriginOuterType> {
    private final Class<T> javaType;

    private final Class<O> originOuterType;

    public Convertor(Class<T> javaType, Class<O> originOuterType) {
        this.javaType = javaType;
        this.originOuterType = originOuterType;
    }

    /**
     * Description 查询结果转化成java类型
     *
     * @author zhaolaiyuan
     * Date 2021/10/30 10:21
     **/
    public abstract T convertToJavaType(Object outerValue);

    /**
     * Description 转化成外部类型，因为外部类型多为语句，所以就是string类型
     *
     * @author zhaolaiyuan
     * Date 2021/10/30 10:20
     **/
    public abstract String convertToStatement(T innerValue);

    public final Class<T> getJavaType() {
        return javaType;
    }

    public final Class<O> getOriginOuterType() {
        return originOuterType;
    }
}
