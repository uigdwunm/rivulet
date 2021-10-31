package vkllyr.jpaminus.utils;

import java.util.Arrays;

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
}
