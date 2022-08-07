package zly.rivulet.base.utils.collector;

import zly.rivulet.base.utils.Constant;

public class FixedLengthStatementCollector implements StatementCollector {
    private final char[] value;

    private int index = 0;

    private static final char leftBracket = '(';

    private static final char rightBracket = ')';

    public FixedLengthStatementCollector(int length) {
        this.value = new char[length];
    }

    @Override
    public StatementCollector leftBracket() {
        return this.append(leftBracket);
    }

    @Override
    public StatementCollector rightBracket() {
        return this.append(rightBracket);
    }

    @Override
    public StatementCollector space() {
        return this.append(Constant.SPACE);
    }

    @Override
    public StatementCollector append(Object o) {
        return this.append(o.toString());
    }

    @Override
    public StatementCollector append(float f) {
        return this.append(Float.toString(f));
    }

    @Override
    public StatementCollector append(double d) {
        return this.append(Double.toString(d));
    }

    @Override
    public StatementCollector append(long l) {
        return this.append(Long.toString(l));
    }

    @Override
    public StatementCollector append(int i) {
        return this.append(Integer.toString(i));
    }

    @Override
    public StatementCollector append(char c) {
        value[index++] = c;
        return this;
    }

    @Override
    public StatementCollector append(boolean b) {
        if (b) {
            value[index++] = 't';
            value[index++] = 'r';
            value[index++] = 'u';
            value[index++] = 'e';
        } else {
            value[index++] = 'f';
            value[index++] = 'a';
            value[index++] = 'l';
            value[index++] = 's';
            value[index++] = 'e';
        }
        return this;
    }

    @Override
    public StatementCollector append(char[] chars) {
        int length = chars.length;
        System.arraycopy(chars, 0, value, index, length);
        this.index += length;
        return this;
    }

    @Override
    public StatementCollector append(String str) {
        int length = str.length();
        str.getChars(0, length, value, index);
        this.index += length;
        return this;
    }


    @Override
    public String toString() {
        return new String(this.value);
    }

}
