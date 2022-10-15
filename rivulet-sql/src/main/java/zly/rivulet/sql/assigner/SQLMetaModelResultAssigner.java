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

//    public ModelAbstractSQLQueryResultAssigner(ModelProxyNode modelProxyNode) {
//        super(assigner, modelProxyNode.getFromModelClass());
//        SQLModelMeta modelMeta = modelProxyNode.getModelMeta();
//        QueryProxyNode parentNode = modelProxyNode.getParentNode();
//        List<SetMapping<Object, Object>> fieldAssignerList = new ArrayList<>();
//        for (FieldMeta sqlFieldMeta : modelMeta.getFieldMetaList()) {
//            Field field = sqlFieldMeta.getField();
//            field.setAccessible(true);
//            SetMapping<Object, Object> assigner = (outerContainer, o) -> {
//                try {
//                    field.set(outerContainer, o);
//                } catch (IllegalAccessException e) {
//                    throw new RuntimeException(e);
//                }
//            };
//            fieldAssignerList.add(assigner);
//            FieldDefinition fieldDefinition = new FieldDefinition(parentNode.getAliasFlag(), modelMeta, sqlFieldMeta);
//            SQLAliasManager.AliasFlag aliasFlag = SQLAliasManager.createFieldAlias(sqlFieldMeta.getFieldName());
//            parentNode.addSelectNode(new FieldProxyNode(parentNode, aliasFlag));
//            modelProxyNode.addMappingDefinition(new MapDefinition(fieldDefinition, parentNode.getAliasFlag(), aliasFlag));
//        }
//        this.fieldAssignerList = View.create(fieldAssignerList);
//    }

    public SQLMetaModelResultAssigner(Class<?> selectModel, List<SetMapping<Object, Object>> setMappingList) {
        super(selectModel);
        this.fieldAssignerList = View.create(setMappingList);
    }

    @Override
    public void assign(Object container, ResultSet resultSet, int indexStart) {
        int size = fieldAssignerList.size();
        for (int i = 0; i < size; i++) {
            SetMapping<Object, Object> setMapping = fieldAssignerList.get(i);
            try {
                setMapping.setMapping(container, resultSet.getObject(indexStart + i + 1));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int size() {
        return fieldAssignerList.size();
    }

}