package zly.rivulet.base.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringJoiner;

public class FormatCollector {

    private final LinkedList<StringBuilder> allLine = new LinkedList<>();

    private StringBuilder currLine = new StringBuilder();

    private int currLineTab;

    private static final char space = ' ';

    // tab
    private static final String TAB = "    ";

    private static final char leftBracket = '(';

    private static final char rightBracket = ')';

    private boolean isFinished = false;

    public FormatCollector() {
        allLine.add(currLine);
    }

    public void leftBracketLine() {
        currLine.append(leftBracket);
        this.line();
    }

    public void rightBracketLine() {
        this.line();
        currLine.append(rightBracket);
        this.line();
    }

    /**
     * 换行
     **/
    public void line() {
        currLine = new StringBuilder();
        for (int i = 0; i < currLineTab; i++) {
            currLine.append(TAB);
        }
        allLine.add(currLine);
    }

    public void tab() {
        currLineTab++;
        currLine.append(TAB);
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

    public <T> JoinHelper<T> createBeforeLineConnectorJoiner(String beforeConnector, Collection<T> collection) {
        return new JoinHelper<>(beforeConnector, Constant.EMPTY, collection.iterator());
    }

    public <T> JoinHelper<T> createAfterLineConnectorJoiner(String afterConnector, Collection<T> collection) {
        return new JoinHelper<>(Constant.EMPTY, afterConnector, collection.iterator());
    }

    public <T> JoinHelper<T> createLineJoiner(String beforeConnector, String afterConnector, Collection<T> collection) {
        return new JoinHelper<>(beforeConnector, afterConnector, collection.iterator());
    }

    public <T> JoinHelper<T> createLineJoiner(Collection<T> collection) {
        return new JoinHelper<>(Constant.EMPTY, Constant.EMPTY, collection.iterator());
    }

    public FormatCollector space() {
        return append(space);

    }

    public class JoinHelper<T> implements Iterable<T> {

        private final Iterator<T> iterator;

        private final char[] beforeLineConnector;

        private final char[] afterLineConnector;

        private JoinHelper(String beforeLineConnector, String afterLineConnector, Iterator<T> iterator) {
            this.beforeLineConnector = beforeLineConnector.toCharArray();
            this.afterLineConnector = afterLineConnector.toCharArray();
            this.iterator = iterator;
        }

        @Override
        public Iterator<T> iterator() {
            return new JoinIterator<>(beforeLineConnector, afterLineConnector, iterator);
        }
    }

    private class JoinIterator<T> implements Iterator<T> {

        private final Iterator<T> iterator;

        private final char[] beforeLineConnector;

        private final char[] afterLineConnector;

        private boolean isFirst = true;

        private JoinIterator(char[] beforeLineConnector, char[] afterLineConnector, Iterator<T> iterator) {
            this.beforeLineConnector = beforeLineConnector;
            this.afterLineConnector = afterLineConnector;
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
                append(beforeLineConnector);
                line();
                append(afterLineConnector);
            }
            return iterator.next();
        }
    }


    @Override
    public String toString() {
        StringJoiner allLine = new StringJoiner(System.lineSeparator());
        for (StringBuilder line : this.allLine) {
//            if (StringUtil.isNotBlank(line)) {
                allLine.add(line);
//            }
        }
        return allLine.toString();
    }
}
