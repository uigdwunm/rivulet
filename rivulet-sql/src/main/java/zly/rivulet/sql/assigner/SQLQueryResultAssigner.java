package zly.rivulet.sql.assigner;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.ResultConvertor;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.exception.SQLModelDefineException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SQLQueryResultAssigner implements Assigner<ResultSet> {

    /**
     * 创建当前容器的
     **/
    private final Supplier<?> containerCreator;

    /**
     * 把每个字段塞到当前model的赋值器
     **/
    private final View<FieldAssignerWrap> fieldAssignerWrapList;

    public SQLQueryResultAssigner(ConvertorManager convertorManager, Class<?> selectModel, List<SetMapping<Object, Object>> setMappingList) {
        this.containerCreator = this.buildContainerCreator(selectModel);
        List<FieldAssignerWrap> wrpaList = setMappingList.stream()
                .map(setMapping -> new FieldAssignerWrap(setMapping, convertorManager))
                .collect(Collectors.toList());
        this.fieldAssignerWrapList = View.create(wrpaList);
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

    public void assign(Object container, ResultSet resultSet, int indexStart) {
        int size = fieldAssignerWrapList.size();
        try {
            for (int i = 0; i < size; i++) {
                FieldAssignerWrap fieldAssignerWrap = fieldAssignerWrapList.get(i);
                SetMapping<Object, Object> setMapping = fieldAssignerWrap.getSetMapping();
                Object result = resultSet.getObject(indexStart + i + 1);
                if (result != null) {
                    ResultConvertor<Object, Object> resultConvertor = fieldAssignerWrap.getConvertor(result.getClass());
                    setMapping.setMapping(container, resultConvertor.convert(result));
                } else {
                    setMapping.setMapping(container, null);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        return fieldAssignerWrapList.size();
    }

    @Override
    public Object getValue(ResultSet results, int indexStart) {
        Object container = this.buildContainer();
        this.assign(container, results, indexStart);
        return container;
    }

    private static class FieldAssignerWrap {

        private final SetMapping<Object, Object> setMapping;

        private final ConvertorManager convertorManager;

        private ResultConvertor<Object, Object> resultConvertor;

        private FieldAssignerWrap(SetMapping<Object, Object> setMapping, ConvertorManager convertorManager) {
            this.setMapping = setMapping;
            this.convertorManager = convertorManager;
        }

        public ResultConvertor<Object, Object> getConvertor(Class<?> originType) {
            if (this.resultConvertor == null) {
                this.resultConvertor = (ResultConvertor) convertorManager.getResultConvertor(originType, setMapping.parseTargetGenericType());
            }
            return this.resultConvertor;
        }

        public SetMapping<Object, Object> getSetMapping() {
            return setMapping;
        }
    }
}
