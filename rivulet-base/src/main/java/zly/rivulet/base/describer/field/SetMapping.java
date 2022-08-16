package zly.rivulet.base.describer.field;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Description 代表返回值对象映射的字段，一定对应返回值对象中的set方法
 * @param <S> 容器对象
 * @param <F> 字段
 *
 * @author zhaolaiyuan
 * Date 2022/1/3 14:07
 **/
@FunctionalInterface
public interface SetMapping<S, F> extends Serializable {

    void setMapping(S s, F f);


    /**
     * Description 解析出当前函数式方法通过lambda双冒号语法写的的方法名
     *
     * @author zhaolaiyuan
     * Date 2022/8/16 8:51
     **/
    default String parseExecuteMethodName() {
        try {
            Method method = this.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda lambda = (SerializedLambda) method.invoke(this);
            return lambda.getImplMethodName();
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
