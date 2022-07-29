package zly.rivulet.base.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.StringJoiner;

public class FormatCollector {

    private final StringJoiner allLine = new StringJoiner(System.lineSeparator());

    private StringBuilder currLine = new StringBuilder();

    private int currLineTab;

    private static final char space = ' ';

    // tab
    private static final String TAB = "    ";

    private static final char leftBracket = '(';

    private static final char rightBracket = ')';

    public void leftBracketLine() {
        currLine.append(leftBracket);
        this.line();
    }

    public void rightBracketLine() {
        currLine.append(rightBracket);
        this.line();
    }

    /**
     * 换行
     **/
    public void line() {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < currLineTab; i++) {
            line.append(TAB);
        }
        line.append(currLine);
        allLine.add(line);
        currLine = new StringBuilder();
    }

    public void tab() {
        currLineTab++;
    }

    public void returnTab() {
        currLineTab--;
    }

    public FormatCollector append(String str) {
        currLine.append(str);
        return this;
    }

    public FormatCollector append(StringBuilder str) {
        currLine.append(str);
        return this;
    }

    public FormatCollector append(int str) {
        currLine.append(str);
        return this;
    }

    public FormatCollector append(long str) {
        currLine.append(str);
        return this;
    }

    public FormatCollector append(boolean str) {
        currLine.append(str);
        return this;
    }

    public FormatCollector append(char str) {
        currLine.append(str);
        return this;
    }

    public FormatCollector append(char[] chars) {
        currLine.append(chars);
        return this;
    }

    public <T> JoinHelper<T> createLineJoiner(String connector, Collection<T> collection) {
        return new JoinHelper<>(connector, collection.iterator());
    }

    public FormatCollector space() {
        return append(space);

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
            boolean hasNext = iterator.hasNext();
            if (!hasNext) {
                this.finish();
            }
            return hasNext;
        }

        public void finish() {
            line();
        }

        @Override
        public T next() {
            if (isFirst) {
                // 第一次不加连接符
                this.isFirst = false;
            } else {
                line();
                append(connector);
            }
            return iterator.next();
        }
    }

    @Override
    public String toString() {
        if (currLine.length() > 0) {
            this.line();
        }
        return allLine.toString();
    }
}
