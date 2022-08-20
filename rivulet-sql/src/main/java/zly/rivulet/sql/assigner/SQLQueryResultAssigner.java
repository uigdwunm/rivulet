package zly.rivulet.sql.assigner;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.sql.exception.SQLModelDefineException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.util.function.Supplier;

public abstract class SQLQueryResultAssigner implements Assigner<ResultSet> {
    /**
     * 把自己塞到父对象的assigner
     **/
    private SetMapping<Object, Object> assigner;

    /**
     * 创建当前容器的
     **/
    private final Supplier<?> containerCreator;

    /**
     * 在select中的位置索引起始
     **/
    protected final int indexStart;

    protected SQLQueryResultAssigner(Class<?> modelClass, int indexStart) {
        this.containerCreator = this.buildContainerCreator(modelClass);
        this.indexStart = indexStart;
    }

    private Supplier<?> buildContainerCreator(Class<?> modelClass) {
        try {
            Constructor<?> constructor = modelClass.getConstructor();
            constructor.setAccessible(true);
            return () -> {
                try {
                    return constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (NoSuchMethodException e) {
            // 必须有无参构造方法
            throw SQLModelDefineException.needNoArgumentsConstructor();
        }
    }

    Object buildContainer() {
        return containerCreator.get();
    }

    public void assign(Object parentContainer, ResultSet resultSet) {
        Object o = this.assign(resultSet);
        // 把自己塞到父容器
        assigner.setMapping(parentContainer, o);
    }

    public abstract Object assign(ResultSet resultSet);

    public abstract int size();

    public void setAssigner(SetMapping<Object, Object> assigner) {
        this.assigner = assigner;
    }
}
