package zly.rivulet.base.utils.collector;

import java.util.Collection;
import java.util.Iterator;

/**
 * Description 语句收集器
 *
 * @author zhaolaiyuan
 * Date 2022/7/24 11:10
 **/
public interface StatementCollector {
    StatementCollector leftBracket();

    StatementCollector rightBracket();

    StatementCollector space();

    StatementCollector append(Object o);

    StatementCollector append(float f);

    StatementCollector append(double d);

    StatementCollector append(long l);

    StatementCollector append(int i);

    StatementCollector append(char c);

    StatementCollector append(boolean b);

    StatementCollector append(char[] chars);

    StatementCollector append(String str);

    default <T> Iterable<T> createJoiner(String connector, Collection<T> collection) {
        return new JoinHelper<>(connector, collection.iterator(), this);
    }

    class JoinHelper<T> implements Iterable<T> {

        private final Iterator<T> iterator;

        private final char[] connector;

        private final StatementCollector statementCollector;

        private JoinHelper(String connector, Iterator<T> iterator, StatementCollector statementCollector) {
            this.connector = connector.toCharArray();
            this.iterator = iterator;
            this.statementCollector = statementCollector;
        }

        @Override
        public Iterator<T> iterator() {
            return new JoinIterator<>(connector, iterator, statementCollector);
        }
    }

    class JoinIterator<T> implements Iterator<T> {

        private final Iterator<T> iterator;

        private final char[] connector;

        private final StatementCollector statementCollector;

        private boolean isFirst = true;

        private JoinIterator(char[] connector, Iterator<T> iterator, StatementCollector statementCollector) {
            this.connector = connector;
            this.iterator = iterator;
            this.statementCollector = statementCollector;
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
                statementCollector.append(connector);
            }
            return iterator.next();
        }
    }
}
