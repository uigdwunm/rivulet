package zly.rivulet.sql.assigner;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.View;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


public class SQLMetaModelResultAssigner extends SQLQueryResultAssigner {
    /**
     * 把每个字段塞到当前model的赋值器
     **/
    private final View<FieldAssignerWrap> fieldAssignerWrapList;


    public SQLMetaModelResultAssigner(ConvertorManager convertorManager, Class<?> selectModel, List<SetMapping<Object, Object>> setMappingList) {
        super(selectModel);
        List<FieldAssignerWrap> wrpaList = setMappingList.stream()
            .map(setMapping -> new FieldAssignerWrap(setMapping, convertorManager))
            .collect(Collectors.toList());
        this.fieldAssignerWrapList = View.create(wrpaList);
    }

    @Override
    public void assign(Object container, ResultSet resultSet, int indexStart) {
        int size = fieldAssignerWrapList.size();
        try {
            // TODO 暂时先放在这
            resultSet.next();
            for (int i = 0; i < size; i++) {
                FieldAssignerWrap fieldAssignerWrap = fieldAssignerWrapList.get(i);
                SetMapping<Object, Object> setMapping = fieldAssignerWrap.getSetMapping();
                Object result = resultSet.getObject(indexStart + i + 1);
                Convertor<Object, Object> convertor = fieldAssignerWrap.getConvertor(result.getClass());
                setMapping.setMapping(container, convertor.convert(result));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        return fieldAssignerWrapList.size();
    }


    private static class FieldAssignerWrap {

        private final SetMapping<Object, Object> setMapping;

        private final ConvertorManager convertorManager;

        private Convertor<Object, Object> convertor;

        private FieldAssignerWrap(SetMapping<Object, Object> setMapping, ConvertorManager convertorManager) {
            this.setMapping = setMapping;
            this.convertorManager = convertorManager;
        }

        public Convertor<Object, Object> getConvertor(Class<?> originType) {
            if (this.convertor == null) {
                Type[] classGenericTypes = ClassUtils.getClassGenericTypes(setMapping.getClass());
                this.convertor = convertorManager.getResultConvertor((Class)originType, (Class)classGenericTypes[1]);
            }
            return this.convertor;

        }

        public SetMapping<Object, Object> getSetMapping() {
            return setMapping;
        }
    }

}
