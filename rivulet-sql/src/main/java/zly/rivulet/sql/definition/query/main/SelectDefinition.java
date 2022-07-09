package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractContainerDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.Desc;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.SelectMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.mapping.MappingDefinition;
import zly.rivulet.sql.describer.function.MFunctionDesc;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.mapper.Assigner;
import zly.rivulet.sql.mapper.ContainerAssigner;
import zly.rivulet.sql.mapper.ModelAssigner;
import zly.rivulet.sql.mapper.SqlMapDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;
import zly.rivulet.sql.preparser.helper.node.FieldProxyNode;
import zly.rivulet.sql.preparser.helper.node.QueryProxyNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description
 * 有两种情况，
 * 1，传了mappedItemList，则按照传的走。
 * 2，没传，返回from对象的
 *
 * @author zhaolaiyuan
 * Date 2022/6/5 20:12
 **/
public class SelectDefinition extends AbstractContainerDefinition {

    private final Class<?> selectModel;

    private final View<MappingDefinition> mappingDefinitionList;

    private final SqlMapDefinition mapDefinition;

//    protected SelectDefinition() {
//        super(CheckCondition.IS_TRUE);
//    }

    public SelectDefinition(SqlPreParseHelper sqlPreParseHelper, FromDefinition fromDefinition, Class<?> selectModel, List<? extends Mapping.Item<?, ?, ?>> mappedItemList) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getSqlParamDefinitionManager());
        this.selectModel = selectModel;
        if (mappedItemList == null || mappedItemList.isEmpty()) {
            // 比较select对象和from对象必须是同一个。
            if (!selectModel.equals(fromDefinition.getFromMode())) {
                throw SQLDescDefineException.selectAndFromNoMatch();
            }
            // 结果对象就是查询对象

            QueryProxyNode currNode = sqlPreParseHelper.getCurrNode();
            Assigner assigner = new ContainerAssigner(sqlPreParseHelper, currNode, 0);
            this.mappingDefinitionList = View.create(currNode.getMappingDefinitionList());
            this.mapDefinition = new SqlMapDefinition(assigner);
        } else {
            List<MappingDefinition> mappingDefinitions = new ArrayList<>();
            List<SelectMapping<Object, Object>> selectMappingList = new ArrayList<>();
            // 结果对象是新的vo
            // 这里可能有子查询的情况
            for (Mapping.Item<?, ?, ?> item : mappedItemList) {
                MappingDefinition mappingDefinition = this.createMappingDefinition(sqlPreParseHelper, item);
                mappingDefinitions.add(mappingDefinition);
                selectMappingList.add((SelectMapping<Object, Object>) item.getSelectField());
            }
            Assigner assigner = new ModelAssigner(selectModel, selectMappingList);
            this.mappingDefinitionList = View.create(mappingDefinitions);
            this.mapDefinition = new SqlMapDefinition(assigner);
        }
    }

    private MappingDefinition createMappingDefinition(SqlPreParseHelper sqlPreParseHelper, Mapping.Item<?, ?, ?> item) {
        SingleValueElementDesc<?, ?> desc = item.getDesc();
        if (desc instanceof FieldMapping) {
            QueryProxyNode currNode = sqlPreParseHelper.getCurrNode();
            SingleValueElementDefinition definition = currNode.parseField((FieldMapping<Object, Object>) desc);
            if (definition instanceof FieldDefinition) {
                FieldDefinition fieldDefinition = (FieldDefinition) definition;
                return new MappingDefinition(sqlPreParseHelper, item, fieldDefinition);
            } else if (definition instanceof MappingDefinition) {
                // 如果是vo对象的get映射方法，则会返回MappingDefinition
                return (MappingDefinition) definition;
            }
        } else if (desc instanceof SqlQueryMetaDesc) {
            SqlQueryMetaDesc<?, ?> sqlQueryMetaDesc = (SqlQueryMetaDesc<?, ?>) desc;
            return new MappingDefinition(sqlPreParseHelper, sqlQueryMetaDesc, item.getSelectField());
        } else if (desc instanceof Param) {
            Param<?> param = (Param<?>) desc;
            return new MappingDefinition(sqlPreParseHelper, param, item.getSelectField());
        } else if (desc instanceof MFunctionDesc) {
            MFunctionDesc<?, ?> functionDesc = (MFunctionDesc<?, ?>) desc;
            return new MappingDefinition(sqlPreParseHelper, functionDesc, item.getSelectField());
        }
        throw UnbelievableException.unknownType();
    }

    @Override
    public SelectDefinition forAnalyze() {
        return null;
    }

    public View<MappingDefinition> getMappingDefinitionList() {
        return mappingDefinitionList;
    }

    public SqlMapDefinition getMapDefinition() {
        return mapDefinition;
    }
}