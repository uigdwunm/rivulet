package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractContainerDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.SelectMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.assigner.ContainerSQLAssigner;
import zly.rivulet.sql.assigner.ModelSQLAssigner;
import zly.rivulet.sql.assigner.SQLAssigner;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.describer.function.MFunctionDesc;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;
import zly.rivulet.sql.preparser.helper.node.QueryProxyNode;

import java.util.ArrayList;
import java.util.List;

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

    private final View<MapDefinition> mappingDefinitionList;

    private final SQLAssigner sqlAssigner;

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
            sqlAssigner = new ContainerSQLAssigner(sqlPreParseHelper, currNode, 0);
            this.mappingDefinitionList = View.create(currNode.getMapDefinitionList());
        } else {
            List<MapDefinition> mapDefinitions = new ArrayList<>();
            List<SelectMapping<Object, Object>> selectMappingList = new ArrayList<>();
            // 结果对象是新的vo
            // 这里可能有子查询的情况
            for (Mapping.Item<?, ?, ?> item : mappedItemList) {
                MapDefinition mapDefinition = this.createMappingDefinition(sqlPreParseHelper, item);
                mapDefinitions.add(mapDefinition);
                selectMappingList.add((SelectMapping<Object, Object>) item.getSelectField());
            }
            sqlAssigner = new ModelSQLAssigner(selectModel, selectMappingList);
            this.mappingDefinitionList = View.create(mapDefinitions);
        }
    }

    private MapDefinition createMappingDefinition(SqlPreParseHelper sqlPreParseHelper, Mapping.Item<?, ?, ?> item) {
        SingleValueElementDesc<?, ?> desc = item.getDesc();
        if (desc instanceof FieldMapping) {
            QueryProxyNode currNode = sqlPreParseHelper.getCurrNode();
            SQLSingleValueElementDefinition definition = currNode.parseField((FieldMapping<Object, Object>) desc);
            if (definition instanceof FieldDefinition) {
                FieldDefinition fieldDefinition = (FieldDefinition) definition;
                return new MapDefinition(sqlPreParseHelper, item, fieldDefinition);
            } else if (definition instanceof MapDefinition) {
                // 如果是vo对象的get映射方法，则会返回MappingDefinition
                return (MapDefinition) definition;
            }
        } else if (desc instanceof SqlQueryMetaDesc) {
            SqlQueryMetaDesc<?, ?> sqlQueryMetaDesc = (SqlQueryMetaDesc<?, ?>) desc;
            return new MapDefinition(sqlPreParseHelper, sqlQueryMetaDesc, item.getSelectField());
        } else if (desc instanceof Param) {
            Param<?> param = (Param<?>) desc;
            return new MapDefinition(sqlPreParseHelper, param, item.getSelectField());
        } else if (desc instanceof MFunctionDesc) {
            MFunctionDesc<?, ?> functionDesc = (MFunctionDesc<?, ?>) desc;
            return new MapDefinition(sqlPreParseHelper, functionDesc, item.getSelectField());
        }
        throw UnbelievableException.unknownType();
    }

    @Override
    public SelectDefinition forAnalyze() {
        return null;
    }

    public View<MapDefinition> getMapDefinitionList() {
        return mappingDefinitionList;
    }

    public SQLAssigner getSqlAssigner() {
        return sqlAssigner;
    }

    public Class<?> getSelectModel() {
        return selectModel;
    }
}