package zly.rivulet.mysql.executor;

import zly.rivulet.sql.assigner.SQLAssigner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

public class ResultSetIterable<T> implements Iterable<T> {

    private final ResultSet resultSet;

    private final SQLAssigner assigner;

    /**
     * Description 结束回调
     *
     * @author zhaolaiyuan
     * Date 2022/8/1 8:22
     **/
    private final Consumer<T> finishCallBack;

    public ResultSetIterable(ResultSet resultSet, SQLAssigner assigner) {
        this.resultSet = resultSet;
        this.assigner = assigner;
        this.finishCallBack = null;
    }

    public ResultSetIterable(ResultSet resultSet, SQLAssigner assigner, Consumer<T> finishCallBack) {
        this.resultSet = resultSet;
        this.assigner = assigner;
        this.finishCallBack = finishCallBack;
    }


    @Override
    public Iterator<T> iterator() {


        return new Iterator<T>() {

            private boolean isFinished = false;

            @Override
            public boolean hasNext() {
                try {
                    boolean next = resultSet.next();
                    if (!next) {
                        isFinished = true;
                    }
                    return next;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public T next() {
                T next = (T) assigner.assign(resultSet);
                // 结束回调
                finishCallBack.accept(next);
                return next;
            }
        };
    }
}
