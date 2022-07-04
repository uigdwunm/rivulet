package zly.rivulet.sql.mapper;

import zly.rivulet.base.describer.field.SelectMapping;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.mapping.MappingDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;
import zly.rivulet.sql.preparser.helper.node.FieldProxyNode;
import zly.rivulet.sql.preparser.helper.node.ModelProxyNode;
import zly.rivulet.sql.preparser.helper.node.QueryProxyNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModelAssigner extends Assigner {

    /**
     * 每个字段的赋值器
     **/
    private final View<SelectMapping<Object, Object>> fieldAssignerList;

    public ModelAssigner(ModelProxyNode modelProxyNode, int indexStart) {
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
            modelProxyNode.addMappingDefinition(new MappingDefinition(fieldDefinition, parentNode.getAliasFlag(), aliasFlag));
        }
        this.fieldAssignerList = View.create(fieldAssignerList);
    }

    public ModelAssigner(Class<?> selectModel, List<SelectMapping<Object, Object>> selectMappingList) {
        super(selectModel, 0);
        this.fieldAssignerList = View.create(selectMappingList);
    }

    @Override
    public Object assign(List<Object> resultValues) {
        Object o = containerCreator.get();
        int size = fieldAssignerList.size();
        for (int i = 0; i < size; i++) {
            SelectMapping<Object, Object> selectMapping = fieldAssignerList.get(i);
            selectMapping.setMapping(o, resultValues.get(super.indexStart + i));
        }
        return o;
    }

    @Override
    public int size() {
        return fieldAssignerList.size();
    }
}
