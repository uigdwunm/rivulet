package zly.rivulet.mysql.util;

import zly.rivulet.base.utils.ArrayUtils;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.runparser.statement.operate.AndOperateStatement;
import zly.rivulet.mysql.runparser.statement.operate.OrOperateStatement;
import zly.rivulet.sql.definition.query.join.SQLJoinType;

import java.util.*;

public class FormatStatementCollector implements StatementCollector {
    /**
     * Description 需要在前面插入行的
     *
     * @author zhaolaiyuan
     * Date 2022/8/7 16:29
     **/
    private static Set<String> BEFORE_APPEND_LINE = ArrayUtils.asSet(
        AndOperateStatement.AND_CONNECTOR,
        OrOperateStatement.OR_CONNECTOR,
        SQLJoinType.LEFT_JOIN.getPrefix(),
        SQLJoinType.RIGHT_JOIN.getPrefix(),
        SQLJoinType.INNER_JOIN.getPrefix()

    );

    /**
     * Description 需要在后面插入行的
     *
     * @author zhaolaiyuan
     * Date 2022/8/7 16:29
     **/
    private static Set<String> AFTER_APPEND_LINE = ArrayUtils.asSet(

    );

    /**
     * Description 需要在连接符后面插入行的
     *
     * @author zhaolaiyuan
     * Date 2022/8/7 16:29
     **/
    private final Set<String> BEFORE_CONNECTOR_LINE = ArrayUtils.asSet(
        Constant.SPACE


    );

    /**
     * Description 需要在连接符后面插入行的
     *
     * @author zhaolaiyuan
     * Date 2022/8/7 16:29
     **/
    private final Set<String> AFTER_CONNECTOR_LINE = ArrayUtils.asSet(

    );

    private final LinkedList<StringBuilder> allLine = new LinkedList<>();

    private StringBuilder currLine = new StringBuilder();

    private int currLineTab;


    public FormatStatementCollector() {
        allLine.add(currLine);
    }

    /**
     * 换行
     **/
    private void line() {
        currLine = new StringBuilder();
        for (int i = 0; i < currLineTab; i++) {
            currLine.append(Constant.TAB);
        }
        allLine.add(currLine);
    }

    private void tab() {
        currLineTab++;
        currLine.append(Constant.TAB);
    }

    private void returnTab() {
        currLineTab--;
    }

    @Override
    public FormatStatementCollector append(String str) {
        if (AFTER_APPEND_LINE.contains(str)) {
            this.line();
        }
        currLine.append(str);
        if (BEFORE_APPEND_LINE.contains(str)) {
            this.line();
        }
        return this;
    }

    @Override
    public FormatStatementCollector append(int str) {
        currLine.append(str);
        return this;
    }

    @Override
    public StatementCollector leftBracket() {
        currLine.append(Constant.LEFT_BRACKET);
        this.line();
        this.tab();
        return this;
    }

    @Override
    public StatementCollector rightBracket() {
        this.line();
        this.returnTab();
        currLine.append(Constant.RIGHT_BRACKET);
        this.line();
        return this;
    }

    @Override
    public StatementCollector space() {
        currLine.append(Constant.SPACE);
        return this;
    }

    @Override
    public StatementCollector append(Object o) {
        currLine.append(o);
        return this;
    }

    @Override
    public StatementCollector append(float f) {
        currLine.append(f);
        return this;
    }

    @Override
    public StatementCollector append(double d) {
        currLine.append(d);
        return this;
    }

    @Override
    public FormatStatementCollector append(long str) {
        currLine.append(str);
        return this;
    }

    @Override
    public FormatStatementCollector append(boolean str) {
        currLine.append(str);
        return this;
    }

    @Override
    public FormatStatementCollector append(char str) {
        currLine.append(str);
        return this;
    }

    @Override
    public FormatStatementCollector append(char[] chars) {
        currLine.append(chars);
        return this;
    }

    @Override
    public <T> JoinHelper<T> createJoiner(String connector, Collection<T> collection) {
        return new JoinHelper<>(connector, collection.iterator());
    }


    public class JoinHelper<T> implements Iterable<T> {

        private final Iterator<T> iterator;

        private final String connector;

        private JoinHelper(String connector, Iterator<T> iterator) {
            this.connector = connector;
            this.iterator = iterator;
        }

        @Override
        public Iterator<T> iterator() {
            return new JoinIterator<>(connector, iterator);
        }
    }

    private class JoinIterator<T> implements Iterator<T> {

        private final Iterator<T> iterator;

        private final String connector;

        private boolean isFirst = true;

        private JoinIterator(String connector, Iterator<T> iterator) {
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
                if (BEFORE_CONNECTOR_LINE.contains(connector)) {
                    line();
                }
                append(connector);
                if (AFTER_CONNECTOR_LINE.contains(connector)) {
                    line();
                }
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
