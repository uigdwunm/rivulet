package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractContainerDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.CollectionUtils;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.describer.meta.SQLColumnMeta;
import zly.rivulet.sql.describer.meta.SQLQueryMeta;
import zly.rivulet.sql.describer.meta.SQLTableMeta;
import zly.rivulet.sql.describer.select.item.JoinItem;
import zly.rivulet.sql.describer.select.item.Mapping;
import zly.rivulet.sql.parser.proxy_node.CommonSelectNode;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SQLParserPortableToolbox;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

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

    private SelectDefinition(CheckCondition checkCondition, Class<?> selectModel, View<MapDefinition> mappingDefinitionList) {
        super(checkCondition, null);
        this.selectModel = selectModel;
        this.mappingDefinitionList = mappingDefinitionList;
    }

    public SelectDefinition(
            SQLParserPortableToolbox toolbox,
            FromDefinition fromDefinition,
            Class<?> selectModel,
            List<? extends Mapping<?>> mappedItemList,
            SQLQueryMeta from,
            List<JoinItem> joinList
    ) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        Map<String, List<SQLColumnMeta<?>>> allColumnMap = this.getAllColumnMetaMap(from, joinList);
        if (mappedItemList == null || mappedItemList.isEmpty()) {
            // 没有指定映射项的，则根据映射模型字段，挨个映射字段
            Field[] fields = selectModel.getDeclaredFields();
            Map<Field, >
        } else {
            for (Mapping<?> mapping : mappedItemList) {

            }
        }






        List<MapDefinition> mapDefinitionList = queryProxyNode.getSelectNodeList().stream()
            .map(selectNode -> {
                if (selectNode instanceof QueryProxyNode) {
                    // 子查询，包裹成MapDefinition
                    return new MapDefinition(selectNode.getQuerySelectMeta(), null, selectNode.getAliasFlag());
                } else if (selectNode instanceof CommonSelectNode) {
                    return (MapDefinition) selectNode.getQuerySelectMeta();
                } else {
                    throw UnbelievableException.unknownType();
                }
            }).collect(Collectors.toList());
        this.selectModel = selectModel;
        this.mappingDefinitionList = View.create(mapDefinitionList);

    }

    public SelectDefinition(
            SQLParserPortableToolbox toolbox,
            Class<?> resultModelClass,
            List<? extends Mapping<?>> mappedItemList,
            SQLQueryMeta from,
            List<JoinItem> joinList
    ) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        Map<String, List<SQLColumnMeta<?>>> allColumnMap = this.getAllColumnMetaMap(from, joinList);
        if (mappedItemList == null || mappedItemList.isEmpty()) {
            // 没有指定映射项的，则根据映射模型字段，挨个映射字段
            Field[] fields = resultModelClass.getFields();
            Map<Field, >
        } else {
            for (Mapping<?> mapping : mappedItemList) {
                SingleValueElementDesc<?> singleValueElementDesc = mapping.getSingleValueElementDesc();
                SetMapping<?, ?> mappingField = mapping.getMappingField();
                if (mappingField == null) {
                    // 子查询时，仅声明需要哪些字段的情况
                } else {

                }
            }
        }
    }

    private Map<String, List<SQLColumnMeta<?>>> getAllColumnMetaMap(SQLQueryMeta from, List<JoinItem> joinList) {
        Map<String, List<SQLColumnMeta<?>>> map = new HashMap<>();
        this.getAllColumnMetaMap(map, from);
        if (CollectionUtils.isNotEmpty(joinList)) {
            joinList.stream().map(JoinItem::getJoinTable).forEach(joinTable -> this.getAllColumnMetaMap(map, joinTable));
        }
        return map;
    }

    private void getAllColumnMetaMap(Map<String, List<SQLColumnMeta<?>>> map, SQLQueryMeta sqlTableMeta) {
        for (SQLColumnMeta<?> sqlColumnMeta : sqlTableMeta.getAllColumnMeta()) {
            map.compute(sqlColumnMeta.getName(), (k, v) -> {
                if (v == null) {
                    v = new ArrayList<>();
                }
                v.add(sqlColumnMeta);
                return v;
            });
        }
    }


    public View<MapDefinition> getMapDefinitionList() {
        return mappingDefinitionList;
    }

    public Class<?> getSelectModel() {
        return selectModel;
    }

    @Override
    public Copier copier() {
        return new Copier(selectModel, mappingDefinitionList);
    }

    public class Copier implements Definition.Copier {

        private Class<?> selectModel;

        private View<MapDefinition> mappingDefinitionList;

        private Copier(Class<?> selectModel, View<MapDefinition> mappingDefinitionList) {
            this.selectModel = selectModel;
            this.mappingDefinitionList = mappingDefinitionList;
        }

        public void setSelectModel(Class<?> selectModel) {
            this.selectModel = selectModel;
        }

        public void setMappingDefinitionList(View<MapDefinition> mappingDefinitionList) {
            this.mappingDefinitionList = mappingDefinitionList;
        }

        @Override
        public SelectDefinition copy() {
            return new SelectDefinition(checkCondition, selectModel, mappingDefinitionList);
        }
    }
}