package zly.rivulet.mysql.executor;

import zly.rivulet.sql.assigner.SQLAssigner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class ResultSetIterable<T> implements Iterable<T> {

    private final ResultSet resultSet;

    private final SQLAssigner assigner;

    public ResultSetIterable(ResultSet resultSet, SQLAssigner assigner) {
        this.resultSet = resultSet;
        this.assigner = assigner;
    }


    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                try {
                    return resultSet.next();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public T next() {
                return (T) assigner.assign(resultSet);
            }
        };
    }
}
