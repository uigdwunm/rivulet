package pers.zly.base.convertor;

public interface Convertor<T, O> {

    /**
     * Description 结果转化成java类型
     *
     * @author zhaolaiyuan
     * Date 2021/10/30 10:21
     **/
    T convertToJavaType(Object outerValue);

    /**
     * Description 转化成外部类型，因为外部类型多为语句，所以就是string类型
     *
     * @author zhaolaiyuan
     * Date 2021/10/30 10:20
     **/
    String convertToStatement(Object innerValue);
}
