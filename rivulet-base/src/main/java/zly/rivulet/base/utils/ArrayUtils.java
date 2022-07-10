package zly.rivulet.base.utils;

import zly.rivulet.base.definition.param.ParamDefinitionSQL;

import java.util.Arrays;
import java.util.function.Function;

public class ArrayUtils {

    public static <T> boolean isEmpty(T[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isEmpty(int[] arr) {
        return arr == null || arr.length == 0;
    }

    public static Object[] fill(Object[] originArr, int[] indexes) {
        int length = indexes.length;
        Object[] result = Arrays.copyOf(originArr, length);

        for (int i = 0; i < length; i++) {
            result[i] = originArr[indexes[i]];
        }
        return result;
    }

    public static <T, R> int find(T[] arr, R target, Function<T, R> function) {
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            T item = arr[i];
            if (target.equals(function.apply(item))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Description 获取数组最后一个
     *
     * @author zhaolaiyuan
     * Date 2021/11/6 11:14
     **/
    public static <T> T getLast(T[] arr) {
        int length = arr.length;
        return arr[length - 1];
    }

    public static int length(int[] arr) {
        if (arr == null) {
            return 0;
        }
        return arr.length;
    }

    public static int length(ParamDefinitionSQL[] arr) {
        if (arr == null) {
            return 0;
        }
        return arr.length;
    }
}
