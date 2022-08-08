package zly.rivulet.sql.assigner;

import zly.rivulet.base.describer.field.SelectMapping;
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

public class ModelSQLAssigner extends SQLAssigner {

    /**
     * 每个字段的赋值器
     **/
    private final View<SelectMapping<Object, Object>> fieldAssignerList;

    public ModelSQLAssigner(ModelProxyNode modelProxyNode, int indexStart) {
        super(modelProxyNode.getFromModelClass(), indexStart);
        SQLModelMeta modelMeta = modelProxyNode.getModelMeta();
        QueryProxyNode parentNode = modelProxyNode.getParentNode();
        List<SelectMapping<Object, Object>> fieldAssignerList = new ArrayList<>();
        for (SQLFieldMeta sqlFieldMeta : modelMeta.getFieldMetaList()) {
            Field field = sqlFieldMeta.getField();
            field.setAccessible(true);
            SelectMapping<Object, Object> assigner = (outerContainer, o) -> {
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

    public ModelSQLAssigner(Class<?> selectModel, List<SelectMapping<Object, Object>> selectMappingList) {
        super(selectModel, 0);
        this.fieldAssignerList = View.create(selectMappingList);
    }

    @Override
    public Object assign(ResultSet resultSet) {
        Object o = super.buildContainer();
        int size = fieldAssignerList.size();
        for (int i = 0; i < size; i++) {
            SelectMapping<Object, Object> selectMapping = fieldAssignerList.get(i);
            try {
                selectMapping.setMapping(o, resultSet.getObject(super.indexStart + i + 1));
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
