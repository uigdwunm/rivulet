package zly.rivulet.base.utils;

public class StringUtil {

    public static final String Empty = "";

    public static String capitalizeFirstChar(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("无法处理空字符串");
        }
        char[] chars = str.toCharArray();
        char firstChar = chars[0];
        if (!Character.isLowerCase(firstChar)) {
            throw new IllegalArgumentException("首字母非小写字母");
        }

        chars[0] = (char) (firstChar - 32);

        return new String(chars);
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    public static boolean isBlank(CharSequence str) {
        if (str == null) {
            return true;
        }
        int length = str.length();
        if (length == 0) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static String multiStr(String base, int multi) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < multi; i++) {
            stringBuilder.append(base);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        String abdc = StringUtil.capitalizeFirstChar("zbdc");
        System.out.print(abdc);
    }

    public static String defaultIfBlank(String value, String defaultValue) {
        return isBlank(value) ? defaultValue : value;
    }
}
