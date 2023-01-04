package zly.rivulet.base.describer.field;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
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

    /**
     * Description 解析出当前要转换的目标类型
     *
     * @author zhaolaiyuan
     * Date 2022/8/16 8:51
     **/
    default Class<?> parseTargetGenericType() {
        try {
            Method method = this.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda lambda = (SerializedLambda) method.invoke(this);
            if (lambda.getCapturedArgCount() > 0) {
                Field field = (Field) lambda.getCapturedArg(0);
                return field.getType();
            } else {
                String instantiatedMethodType = lambda.getInstantiatedMethodType();
                String targetGenericTypeName = parseTargetGenericTypeName(instantiatedMethodType);
                return Class.forName(targetGenericTypeName);
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static String parseTargetGenericTypeName(String typeStr) {
        char[] chars = typeStr.toCharArray();
        int start = -1;
        int end = -1;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == ';') {
                if (start == -1) {
                    // 另外过掉 ';'  和  'L'
                    start = i + 1 + 1;
                } else {
                    end = i;
                    break;
                }
            } else if (start != -1 && c == '/') {
                chars[i] = '.';
            }
        }
        return new String(chars, start, end - start);
    }

}
