package zly.rivulet.sql.assigner;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.sql.exception.SQLModelDefineException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.util.function.Supplier;

public abstract class AbstractSQLQueryResultAssigner implements Assigner<ResultSet> {
    /**
     * 把当前model塞到父容器的（如果没有父容器，则为空）
     **/
    private final SetMapping<Object, Object> assigner;

    /**
     * 创建当前容器的
     **/
    private final Supplier<?> containerCreator;

    protected AbstractSQLQueryResultAssigner(SetMapping<Object, Object> assigner, Class<?> modelClass) {
        this.assigner = assigner;
        this.containerCreator = this.buildContainerCreator(modelClass);
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

    public abstract void assign(Object parentContainer, ResultSet resultSet, int indexStart);

    @Override
    public Object getValue(ResultSet results, int indexStart) {
        Object container = this.buildContainer();
        this.assign(container, results, indexStart);
        return container;
    }

    //    public abstract Object assign(ResultSet resultSet, int indexStart);

    public abstract int size();

    public SetMapping<Object, Object> getAssigner() {
        return assigner;
    }
}
