package zly.rivulet.sql.mapper;

import zly.rivulet.base.describer.field.SelectMapping;
import zly.rivulet.sql.exception.SQLModelDefineException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Supplier;

public abstract class Assigner {
    /**
     * 把自己塞到父对象的assigner
     **/
    private SelectMapping<Object, Object> assigner;

    /**
     * 创建当前容器的
     **/
    protected final Supplier<?> containerCreator;

    /**
     * 在select中的位置索引起始
     **/
    protected final int indexStart;

    protected Assigner(Class<?> modelClass, int indexStart) {
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

    public Object assign(Object parentContainer, List<Object> resultValues) {
        Object o = this.assign(resultValues);
        // 把自己塞到父容器
        assigner.setMapping(parentContainer, o);
        return o;
    }

    public abstract Object assign(List<Object> resultValues);

    public abstract int size();

    public void setAssigner(SelectMapping<Object, Object> assigner) {
        this.assigner = assigner;
    }
}
