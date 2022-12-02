package zly.rivulet.sql.assigner;

import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.utils.View;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class SQLMetaModelResultAssigner extends SQLQueryResultAssigner {
    /**
     * 把每个字段塞到当前model的赋值器
     **/
    private final View<SetMapping<Object, Object>> fieldAssignerList;

    public SQLMetaModelResultAssigner(Class<?> selectModel, List<SetMapping<Object, Object>> setMappingList) {
        super(selectModel);
        this.fieldAssignerList = View.create(setMappingList);
    }

    @Override
    public void assign(Object container, ResultSet resultSet, int indexStart) {
        int size = fieldAssignerList.size();
        try {
            // TODO 暂时先放在这
            resultSet.next();
            for (int i = 0; i < size; i++) {
                SetMapping<Object, Object> setMapping = fieldAssignerList.get(i);
                Object object = resultSet.getObject(indexStart + i + 1);
                setMapping.setMapping(container, object);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        return fieldAssignerList.size();
    }

}
