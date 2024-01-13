package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractContainerDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.utils.CollectionUtils;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definer.meta.SQLColumnMeta;
import zly.rivulet.sql.definer.meta.SQLQueryMeta;
import zly.rivulet.sql.definition.query.mapping.SelectItemDefinition;
import zly.rivulet.sql.describer.select.item.CommonMapping;
import zly.rivulet.sql.describer.select.item.JoinItem;
import zly.rivulet.sql.describer.select.item.Mapping;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.toolbox.SQLParserPortableToolbox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
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

    private final View<SelectItemDefinition> selectItemList;

    private final List<SetMapping<Object, Object>> setMappingList;

    private SelectDefinition(CheckCondition checkCondition, Class<?> selectModel, View<SelectItemDefinition> selectItemList, List<SetMapping<Object, Object>> setMappingList) {
        super(checkCondition, null);
        this.selectModel = selectModel;
        this.selectItemList = selectItemList;
        this.setMappingList = setMappingList;
    }

    public SelectDefinition(
            SQLParserPortableToolbox toolbox,
            Class<?> selectModel,
            List<? extends Mapping<?>> mappedItemList,
            SQLQueryMeta from,
            List<JoinItem> joinList
    ) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        List<SetMapping<Object, Object>> setMappingList = new ArrayList<>();
        List<SelectItemDefinition> selectItemList = new ArrayList<>();
        if (mappedItemList == null || mappedItemList.isEmpty()) {
            // 没有指定映射项的，则根据映射模型字段，挨个映射字段
            Map<String, List<SQLColumnMeta<?>>> allColumnMap = this.getAllColumnMetaMap(from, joinList);
            for (Field field : selectModel.getDeclaredFields()) {
                String fieldName = field.getName();
                List<SQLColumnMeta<?>> sqlColumnMetas = allColumnMap.get(fieldName);
                if (sqlColumnMetas == null) {
                    continue;
                }
                if (sqlColumnMetas.size() > 1) {
                    // 多个表下存在重名的字段名
                    throw SQLDescDefineException.repeatColumnName(sqlColumnMetas);
                }
                setMappingList.add((s, f) -> {
                    try {
                        field.set(s, f);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
                SQLColumnMeta<?> sqlColumnMeta = sqlColumnMetas.get(0);
                selectItemList.add(new SelectItemDefinition(sqlColumnMeta, null));
            }
        } else {
            for (Mapping<?> mapping : mappedItemList) {
                if (mapping instanceof CommonMapping) {
                    CommonMapping commonMapping = (CommonMapping) mapping;
                    setMappingList.add(commonMapping.getMappingField());
                }
                SelectItemDefinition selectItemDefinition = toolbox.parseSelectItemDefinition(mapping.getSingleValueElementDesc());
                selectItemList.add(selectItemDefinition);
            }
        }

        this.selectModel = selectModel;
        this.selectItemList = View.create(selectItemList);
        this.setMappingList = setMappingList;

    }
//
//    /**
//     * 单表快捷查询专用
//     **/
//    public SelectDefinition(
//            Class<?> selectModel,
//            List<? extends CommonMapping<?>> mappedItemList,
//            SQLQueryMeta from
//    ) {
//        super(CheckCondition.IS_TRUE, null);
//        List<SetMapping<Object, Object>> setMappingList = new ArrayList<>();
//        List<SelectItemDefinition> selectItemList = new ArrayList<>();
//        if (mappedItemList == null || mappedItemList.isEmpty()) {
//            // 没有指定映射项的，则根据映射模型字段，挨个映射字段
//            Map<String, List<SQLColumnMeta<?>>> allColumnMap = new HashMap<>();
//            this.getAllColumnMetaMap(allColumnMap, from);
//
//            for (Field field : selectModel.getDeclaredFields()) {
//                String fieldName = field.getName();
//                List<SQLColumnMeta<?>> sqlColumnMetas = allColumnMap.get(fieldName);
//                if (sqlColumnMetas == null) {
//                    continue;
//                }
//                if (sqlColumnMetas.size() > 1) {
//                    // 多个表下存在重名的字段名
//                    throw SQLDescDefineException.repeatColumnName(sqlColumnMetas);
//                }
//                setMappingList.add((s, f) -> {
//                    try {
//                        field.set(s, f);
//                    } catch (IllegalAccessException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//                SQLColumnMeta<?> sqlColumnMeta = sqlColumnMetas.get(0);
//                selectItemList.add(new SelectItemDefinition(sqlColumnMeta, null));
//            }
//        } else {
//            for (CommonMapping<?> mapping : mappedItemList) {
//                setMappingList.add((SetMapping) mapping.getMappingField());
//                // 表字段类型
//                SQLColumnMeta<?> sqlColumnMeta = (SQLColumnMeta<?>) mapping.getSingleValueElementDesc();
//                SQLQueryMeta sqlQueryMeta = sqlColumnMeta.getSqlQueryMeta();
//                SelectItemDefinition selectItemDefinition = new SelectItemDefinition(sqlColumnMeta, sqlQueryMeta);
//                selectItemList.add(selectItemDefinition);
//            }
//        }
//
//        this.selectModel = selectModel;
//        this.selectItemList = View.create(selectItemList);
//        this.setMappingList = setMappingList;
//    }

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

    public View<SelectItemDefinition> getMapDefinitionList() {
        return selectItemList;
    }

    public Class<?> getSelectModel() {
        return selectModel;
    }

    public List<SetMapping<Object, Object>> getSetMappingList() {
        return setMappingList;
    }

    @Override
    public Copier copier() {
        return new Copier(selectModel, selectItemList, setMappingList);
    }

    public class Copier implements Definition.Copier {

        private final Class<?> selectModel;

        private final View<SelectItemDefinition> selectItemList;

        private final List<SetMapping<Object, Object>> setMappingList;

        private Copier(Class<?> selectModel, View<SelectItemDefinition> selectItemList, List<SetMapping<Object, Object>> setMappingList) {
            this.selectModel = selectModel;
            this.selectItemList = selectItemList;
            this.setMappingList = setMappingList;
        }

        public View<SelectItemDefinition> getSelectItemList() {
            return selectItemList;
        }

        @Override
        public SelectDefinition copy() {
            return new SelectDefinition(checkCondition, selectModel, selectItemList, setMappingList);
        }
    }
}