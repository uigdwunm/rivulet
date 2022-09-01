package zly.rivulet.sql.assigner;

import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.node.FieldProxyNode;
import zly.rivulet.sql.parser.node.ModelProxyNode;
import zly.rivulet.sql.parser.node.QueryProxyNode;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModelSQLQueryResultAssigner extends SQLQueryResultAssigner {

    /**
     * 每个字段的赋值器
     **/
    private final View<SetMapping<Object, Object>> fieldAssignerList;

    public ModelSQLQueryResultAssigner(ModelProxyNode modelProxyNode, int indexStart) {
        super(modelProxyNode.getFromModelClass(), indexStart);
        SQLModelMeta modelMeta = modelProxyNode.getModelMeta();
        QueryProxyNode parentNode = modelProxyNode.getParentNode();
        List<SetMapping<Object, Object>> fieldAssignerList = new ArrayList<>();
        for (FieldMeta sqlFieldMeta : modelMeta.getFieldMetaList()) {
            Field field = sqlFieldMeta.getField();
            field.setAccessible(true);
            SetMapping<Object, Object> assigner = (outerContainer, o) -> {
                try {
                    field.set(outerContainer, o);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            };
            fieldAssignerList.add(assigner);
            FieldDefinition fieldDefinition = new FieldDefinition(parentNode.getAliasFlag(), modelMeta, sqlFieldMeta);
            SQLAliasManager.AliasFlag aliasFlag = SQLAliasManager.createFieldAlias(sqlFieldMeta.getFieldName());
            parentNode.addSelectNode(new FieldProxyNode(parentNode, aliasFlag));
            modelProxyNode.addMappingDefinition(new MapDefinition(fieldDefinition, parentNode.getAliasFlag(), aliasFlag));
        }
        this.fieldAssignerList = View.create(fieldAssignerList);
    }

    public ModelSQLQueryResultAssigner(Class<?> selectModel, List<SetMapping<Object, Object>> setMappingList) {
        super(selectModel, 0);
        this.fieldAssignerList = View.create(setMappingList);
    }

    @Override
    public Object assign(ResultSet resultSet) {
        Object o = super.buildContainer();
        int size = fieldAssignerList.size();
        for (int i = 0; i < size; i++) {
            SetMapping<Object, Object> setMapping = fieldAssignerList.get(i);
            try {
                setMapping.setMapping(o, resultSet.getObject(super.indexStart + i + 1));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return o;
    }

    @Override
    public int size() {
        return fieldAssignerList.size();
    }
}
