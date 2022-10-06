package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractContainerDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.assigner.ContainerSQLQueryResultAssigner;
import zly.rivulet.sql.assigner.ModelSQLQueryResultAssigner;
import zly.rivulet.sql.assigner.SQLQueryResultAssigner;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.describer.function.MFunctionDesc;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;
import zly.rivulet.sql.parser.node.QueryProxyNode;

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

    private final SQLQueryResultAssigner sqlQueryResultAssigner;

//    protected SelectDefinition() {
//        super(CheckCondition.IS_TRUE);
//    }

    public SelectDefinition(SqlParserPortableToolbox toolbox, Class<?> fromModel, Class<?> selectModel) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        this.selectModel = selectModel;
        // 比较select对象和from对象必须是同一个。
        if (!selectModel.equals(fromModel)) {
            throw SQLDescDefineException.selectAndFromNoMatch();
        }
        // 结果对象就是查询对象
        QueryProxyNode currNode = toolbox.getCurrNode();
        this.sqlQueryResultAssigner = new ContainerSQLQueryResultAssigner(toolbox, currNode, 0);
        this.mappingDefinitionList = View.create(currNode.getMapDefinitionList());
    }

    public SelectDefinition(SqlParserPortableToolbox toolbox, Class<?> selectModel, List<? extends Mapping<?, ?, ?>> mappedItemList) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        this.selectModel = selectModel;
        List<MapDefinition> mapDefinitions = new ArrayList<>();
        List<SetMapping<Object, Object>> setMappingList = new ArrayList<>();
        // 结果对象是新的vo
        // 这里可能有子查询的情况
        for (Mapping<?, ?, ?> item : mappedItemList) {
            MapDefinition mapDefinition = this.createMappingDefinition(toolbox, item);
            mapDefinitions.add(mapDefinition);
            setMappingList.add((SetMapping<Object, Object>) item.getMappingField());
        }
        this.sqlQueryResultAssigner = new ModelSQLQueryResultAssigner(selectModel, setMappingList);
        this.mappingDefinitionList = View.create(mapDefinitions);
    }

    private MapDefinition createMappingDefinition(SqlParserPortableToolbox sqlPreParseHelper, Mapping<?, ?, ?> item) {
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
            return new MapDefinition(sqlPreParseHelper, sqlQueryMetaDesc, item.getMappingField());
        } else if (desc instanceof Param) {
            Param<?> param = (Param<?>) desc;
            return new MapDefinition(sqlPreParseHelper, param, item.getMappingField());
        } else if (desc instanceof MFunctionDesc) {
            MFunctionDesc<?, ?> functionDesc = (MFunctionDesc<?, ?>) desc;
            return new MapDefinition(sqlPreParseHelper, functionDesc, item.getMappingField());
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

    public SQLQueryResultAssigner getSqlAssigner() {
        return sqlQueryResultAssigner;
    }

    public Class<?> getSelectModel() {
        return selectModel;
    }
}