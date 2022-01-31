package vkllyr.jpaminus.utils;

import java.util.ArrayList;

public class MStringBuilder {

    private final ArrayList<String> strs = new ArrayList<>();

    private int length;

    private String str;

    public MStringBuilder append(String str) {
        strs.add(str);
        length += str.length();
        return this;
    }

    public int size() {
        return strs.size();
    }

    public int charSize() {
        return this.length;
    }

    public void set(int index, String str) {
        strs.add(index, str);
    }

    public void append(String[] strs) {
        for (String str : strs) {
            this.append(str);
        }
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
