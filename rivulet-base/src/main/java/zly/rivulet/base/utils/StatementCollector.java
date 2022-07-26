package zly.rivulet.base.utils;

import java.util.Collection;
import java.util.Iterator;

/**
 * Description 定长的语句收集器
 *
 * @author zhaolaiyuan
 * Date 2022/7/24 11:10
 **/
public class StatementCollector {
    private final char[] value;

    private int index = 0;

    public static final String SPACE = " ";

    private static final char space = ' ';

    private static final char leftBracket = '(';

    private static final char rightBracket = ')';

    public StatementCollector(int length) {
        this.value = new char[length];
    }

    public StatementCollector leftBracket() {
        return this.append(leftBracket);
    }

    public StatementCollector rightBracket() {
        return this.append(rightBracket);
    }

    public StatementCollector space() {
        return this.append(space);
    }

    public StatementCollector append(Object o) {
        return this.append(o.toString());
    }

    public StatementCollector append(float f) {
        return this.append(Float.toString(f));
    }

    public StatementCollector append(double d) {
        return this.append(Double.toString(d));
    }

    public StatementCollector append(Long l) {
        return this.append(Long.toString(l));
    }

    public StatementCollector append(int i) {
        return this.append(Integer.toString(i));
    }

    public StatementCollector append(char c) {
        value[index++] = c;
        return this;
    }

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

    public StatementCollector append(char[] chars) {
        int length = chars.length;
        System.arraycopy(chars, 0, value, index, length);
        this.index += length;
        return this;
    }

    public StatementCollector append(String str) {
        int length = str.length();
        str.getChars(0, length, value, index);
        this.index += length;
        return this;
    }

    public <T> JoinHelper<T> createJoiner(String connector, Collection<T> collection) {
        return new JoinHelper<>(connector, collection.iterator());
    }

    @Override
    public String toString() {
        return new String(this.value);
    }

    public class JoinHelper<T> implements Iterable<T> {

        private final Iterator<T> iterator;

        private final char[] connector;

        private JoinHelper(String connector, Iterator<T> iterator) {
            this.connector = connector.toCharArray();
            this.iterator = iterator;
        }

        @Override
        public Iterator<T> iterator() {
            return new JoinIterator<>(connector, iterator);
        }
    }

    private class JoinIterator<T> implements Iterator<T> {

        private final Iterator<T> iterator;

        private final char[] connector;

        private boolean isFirst = true;

        private JoinIterator(char[] connector, Iterator<T> iterator) {
            this.connector = connector;
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            if (isFirst) {
                // 第一次不加连接符
                this.isFirst = false;
            } else {
                append(connector);
            }
            return iterator.next();
        }
    }
}
