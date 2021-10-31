package vkllyr.jpaminus.utils;

import java.util.LinkedList;

public class MStringBuilder {

    private final LinkedList<String> strs = new LinkedList<>();

    private int length;

    private String str;

    public MStringBuilder append(String str) {
        strs.add(str);
        length += str.length();
        return this;
    }

    @Override
    public String toString() {
        if (str != null) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder(length);
        for (String str : strs) {
            stringBuilder.append(str);
        }
        str = stringBuilder.toString();
        return str;
    }
}
