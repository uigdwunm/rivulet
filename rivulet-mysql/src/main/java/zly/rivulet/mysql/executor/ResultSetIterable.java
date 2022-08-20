package zly.rivulet.mysql.executor;

import zly.rivulet.sql.assigner.SQLQueryResultAssigner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class ResultSetIterable<T> implements Iterable<T> {

    private final ResultSet resultSet;

    private final SQLQueryResultAssigner assigner;

    /**
     * Description 结束回调
     *
     * @author zhaolaiyuan
     * Date 2022/8/1 8:22
     **/
    private final Runnable finishCallBack;

    public ResultSetIterable(ResultSet resultSet, SQLQueryResultAssigner assigner) {
        this.resultSet = resultSet;
        this.assigner = assigner;
        this.finishCallBack = null;
    }

    public ResultSetIterable(ResultSet resultSet, SQLQueryResultAssigner assigner, Runnable finishCallBack) {
        this.resultSet = resultSet;
        this.assigner = assigner;
        this.finishCallBack = finishCallBack;
    }


    @Override
    public Iterator<T> iterator() {


        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                try {
                    boolean next = resultSet.next();
                    if (!next) {
                        // 结束回调
                        finishCallBack.run();
                    }
                    return next;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public T next() {
                return  (T) assigner.assign(resultSet);
            }
        };
    }
}
