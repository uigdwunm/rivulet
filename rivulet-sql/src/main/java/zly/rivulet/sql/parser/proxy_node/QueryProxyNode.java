package zly.rivulet.sql.parser.proxy_node;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.CollectionUtils;
import zly.rivulet.base.utils.PairReturn;
import zly.rivulet.sql.assigner.SQLContainerResultAssigner;
import zly.rivulet.sql.assigner.SQLMetaModelResultAssigner;
import zly.rivulet.sql.assigner.SQLQueryResultAssigner;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definer.annotations.SQLSubQuery;
import zly.rivulet.sql.definer.annotations.SqlQueryAlias;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.SqlParser;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

public class QueryProxyNode implements SelectNode, FromNode {

    /**
     * 是SqlQueryDefinition, 解析时没啥用,也不能用,提前放到这
     **/
    private final SqlQueryDefinition sqlQueryDefinition;

    private final Object proxyModel;

    /**
     * 当前query的所有from，如果是单表查询则只有一个，但是不会为空
     **/
    private final List<FromNode> fromNodeList;

    /**
     * 当前query的所有select
     **/
    private final List<SelectNode> selectNodeList;

    private final Assigner<ResultSet> assigner;
    /**
     * 当前节点的别名flag
     **/
    private final SQLAliasManager.AliasFlag aliasFlag = SQLAliasManager.createAlias();


    public QueryProxyNode(SqlQueryDefinition sqlQueryDefinition, SqlParserPortableToolbox toolbox, SqlQueryMetaDesc<?, ?> sqlQueryMetaDesc) {
        this.sqlQueryDefinition = sqlQueryDefinition;
        Class<?> fromModelClass = sqlQueryMetaDesc.getMainFrom();
        Class<?> selectModelClass = sqlQueryMetaDesc.getSelectModel();
        List<? extends Mapping<?, ?, ?>> mappedItemList = sqlQueryMetaDesc.getMappedItemList();

        // 根据from模型和select方式的不同，有多种解析路径
        if (ClassUtils.isExtend(QueryComplexModel.class, fromModelClass)) {
            // from是复杂查询模型
            // 复杂查询模型还需要填充这个，解析select可能会用到
            Map<FromNode, Field> fromNodeFieldMap = new HashMap<>();
            this.proxyModel = this.createProxyByComplexModel(fromModelClass, aliasFlag);
            this.fromNodeList = this.parseFromNodeListByComplexModel(toolbox, proxyModel, fromModelClass, fromNodeFieldMap);

            if (CollectionUtils.isEmpty(mappedItemList)) {
                // 预先指定的结果映射
                // 通过指定好的映射解析
                selectNodeList = this.parseSelectNodeListByMappedItemList(toolbox, aliasFlag, proxyModel, mappedItemList);
                // 生成赋值器
                assigner = this.parseAssignerByMappedItemList(selectModelClass, mappedItemList);
            } else if (fromModelClass.equals(selectModelClass)) {
                PairReturn<List<SelectNode>, Assigner<ResultSet>> pairReturn = this.parseSelectNodeListAndAssignerByComplexModel(fromNodeList, selectModelClass, fromNodeFieldMap);
                selectNodeList = pairReturn.getLeft();
                assigner = pairReturn.getRight();
            } else {
                // 两种情况都不是，抛异常
                throw SQLDescDefineException.selectAndFromNoMatch();
            }
        } else {
            // from是表模型
            MetaModelProxyNode metaModelProxyNode = new MetaModelProxyNode(toolbox, fromModelClass);
            proxyModel = metaModelProxyNode.getProxyModel();
            fromNodeList = Collections.singletonList(metaModelProxyNode);

            if (CollectionUtils.isEmpty(mappedItemList)) {
                // 预先指定的结果映射
                // 通过指定好的映射解析
                selectNodeList = this.parseSelectNodeListByMappedItemList(toolbox, aliasFlag, proxyModel, mappedItemList);
                // 生成赋值器
                assigner = this.parseAssignerByMappedItemList(selectModelClass, mappedItemList);
            } else if (fromModelClass.equals(selectModelClass)) {
                PairReturn<List<SelectNode>, SQLQueryResultAssigner> pairReturn = this.parseSelectNodeListAndAssignerByMetaModel(metaModelProxyNode);
                selectNodeList = pairReturn.getLeft();
                assigner = pairReturn.getRight();
            } else {
                // 两种情况都不是，抛异常
                throw SQLDescDefineException.selectAndFromNoMatch();
            }
        }
    }

    private PairReturn<List<SelectNode>, Assigner<ResultSet>> parseSelectNodeListAndAssignerByComplexModel(
        List<FromNode> fromNodeList,
        Class<?> selectModelClass,
        Map<FromNode, Field> fromNodeFieldMap
    ) {
        List<SelectNode> selectNodeList = new ArrayList<>();
        List<SQLContainerResultAssigner.ContainerFieldAssignerWrap> assignerList = new ArrayList<>();
        for (FromNode fromNode : fromNodeList) {
            Field field = fromNodeFieldMap.get(fromNode);
            SQLQueryResultAssigner fromItemAssigner;
            if (fromNode instanceof MetaModelProxyNode) {
                MetaModelProxyNode metaModelProxyNode = (MetaModelProxyNode) fromNode;
                PairReturn<List<SelectNode>, SQLQueryResultAssigner> pairReturn = this.parseSelectNodeListAndAssignerByMetaModel(metaModelProxyNode);
                selectNodeList.addAll(pairReturn.getLeft());
                // 生成模型的赋值器
                fromItemAssigner = pairReturn.getRight();
            } else if (fromNode instanceof QueryProxyNode) {
                QueryProxyNode queryProxyNode = (QueryProxyNode) fromNode;
                SqlQueryDefinition sqlQueryDefinition = queryProxyNode.getQuerySelectMeta();
                // 拿到子查询的selectNode
                selectNodeList.addAll(queryProxyNode.getSelectNodeList());
                // 拿到子查询的赋值器
                fromItemAssigner = sqlQueryDefinition.getAssigner();
            } else {
                throw UnbelievableException.unknownType();
            }
            SQLContainerResultAssigner.ContainerFieldAssignerWrap assignerWrap = new SQLContainerResultAssigner.ContainerFieldAssignerWrap(
                fromItemAssigner,
                (s, f) -> {
                    try {
                        field.set(s, f);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
            assignerList.add(assignerWrap);
        }

        // 生成复杂容器模型的赋值器
        SQLContainerResultAssigner assigner = new SQLContainerResultAssigner(selectModelClass, assignerList);
        return PairReturn.of(selectNodeList, assigner);
    }

    private PairReturn<List<SelectNode>, SQLQueryResultAssigner> parseSelectNodeListAndAssignerByMetaModel(MetaModelProxyNode metaModelProxyNode) {
        List<SelectNode> selectNodeList = new ArrayList<>();
        SQLModelMeta queryFromMeta = metaModelProxyNode.getQueryFromMeta();
        List<SetMapping<Object, Object>> setMappingList = new ArrayList<>();
        // 循环模型的所有字段
        for (FieldMeta fieldMeta : queryFromMeta.getFieldMetaList()) {
            Field field = fieldMeta.getField();
            SQLAliasManager.AliasFlag fieldAliasFlag = SQLAliasManager.createAlias(fieldMeta.getOriginName());
            // 生成模型字段的selectNode
            MapDefinition mapDefinition = new MapDefinition((SQLFieldMeta) fieldMeta, metaModelProxyNode.getAliasFlag(), fieldAliasFlag);
            CommonSelectNode commonSelectNode = new CommonSelectNode(fieldAliasFlag, mapDefinition);
            selectNodeList.add(commonSelectNode);
            setMappingList.add((s, f) -> {
                try {
                    field.set(s, f);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        // 生成模型的赋值器
        SQLMetaModelResultAssigner assigner = new SQLMetaModelResultAssigner(queryFromMeta.getModelClass(), setMappingList);
        return PairReturn.of(selectNodeList, assigner);
    }

    private List<FromNode> parseFromNodeListByComplexModel(
        SqlParserPortableToolbox toolbox,
        Object proxyModel,
        Class<?> fromModelClass,
        Map<FromNode, Field> fromNodeFieldMap
    ) {
        List<FromNode> fromNodeList = new ArrayList<>();
        SQLAliasManager sqlAliasManager = toolbox.getSqlAliasManager();

        // 每个字段注入代理对象
        for (Field field : fromModelClass.getDeclaredFields()) {
            // 解析出fromNode
            FromNode fromNode = this.parseFromNode(toolbox, field);

            // 指定别名
            SqlQueryAlias sqlQueryAlias = field.getAnnotation(SqlQueryAlias.class);
            if (sqlQueryAlias != null) {
                SQLAliasManager.AliasFlag fromNodeAliasFlag = fromNode.getAliasFlag();
                if (sqlQueryAlias.isForce()) {
                    sqlAliasManager.forceAlias(fromNodeAliasFlag, sqlQueryAlias.value());
                } else {
                    sqlAliasManager.suggestAlias(fromNodeAliasFlag, field.getName());
                }
            }
            // 记录到fromList中
            fromNodeList.add(fromNode);
            // 维护fromNode和field的解析，后面解析select时会用到
            fromNodeFieldMap.put(fromNode, field);

            // 注入到代理对象字段
            field.setAccessible(false);
            try {
                field.set(proxyModel, fromNode.getProxyModel());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return fromNodeList;
    }

    private FromNode parseFromNode(SqlParserPortableToolbox toolbox, Field field) {
        SqlParser sqlParser = toolbox.getSqlPreParser();
        Class<?> fieldType = field.getType();
        // 这个注解表示当前字段代表一个子查询，value是key
        SQLSubQuery sqlSubQuery = field.getAnnotation(SQLSubQuery.class);
        if (sqlSubQuery != null) {
            // 解析子查询
            WholeDesc wholeDesc = sqlParser.getWholeDesc(sqlSubQuery.value());
            // 校验下
            if (wholeDesc instanceof SqlQueryMetaDesc) {
                SqlQueryMetaDesc<Object, Object> sqlQueryMetaDesc = (SqlQueryMetaDesc<Object, Object>) wholeDesc;
                // 校验子查询的模型是否等于原desc的from模型
                if (!fieldType.equals(sqlQueryMetaDesc.getMainFrom())) {
                    throw SQLDescDefineException.subQueryMustOriginFrom(sqlSubQuery.value(), fieldType, sqlQueryMetaDesc.getMainFrom());
                }
                if (!sqlQueryMetaDesc.getMainFrom().equals(sqlQueryMetaDesc.getSelectModel())) {
                    // 如果原desa的from模型和select模型不匹配，则重新改造一个
                    Class<Object> mainFrom = (Class<Object>) sqlQueryMetaDesc.getMainFrom();
                    wholeDesc = new SqlQueryMetaDesc<>(
                        mainFrom,
                        mainFrom,
                        null,
                        sqlQueryMetaDesc.getWhereConditionContainer(),
                        sqlQueryMetaDesc.getGroupFieldList(),
                        sqlQueryMetaDesc.getHavingItemList(),
                        sqlQueryMetaDesc.getOrderFieldList(),
                        sqlQueryMetaDesc.getSkit(),
                        sqlQueryMetaDesc.getLimit()
                    );
                }
            } else {
                throw SQLDescDefineException.mustQueryKey(sqlSubQuery.value(), fieldType);
            }
            // 解析子查询
            sqlParser.parseByDesc(wholeDesc, toolbox);
            return toolbox.popQueryProxyNode();
        } else {
            SqlDefiner sqlDefiner = sqlParser.getDefiner();
            // 不是子查询, 则按modelMeta解析
            SQLModelMeta sqlModelMeta = sqlDefiner.createOrGetModelMeta(field.getType());
            if (sqlModelMeta == null) {
                // 没找到对应的表对象
                throw SQLDescDefineException.unknowQueryType();
            }
            return new MetaModelProxyNode(sqlModelMeta);
        }
    }


    private Object createProxyByComplexModel(Class<?> fromModelClass, SQLAliasManager.AliasFlag aliasFlag) {
        return ClassUtils.dynamicProxy(
            fromModelClass,
            (method, args) -> {
                MapDefinition mapDefinition = THREAD_LOCAL.get();
                if (mapDefinition != null) {
                    THREAD_LOCAL.set(new MapDefinition(mapDefinition, aliasFlag, SQLAliasManager.createAlias()));
                }
            }
        );
    }

    private List<SelectNode> parseSelectNodeListByMappedItemList(
        SqlParserPortableToolbox toolbox,
        SQLAliasManager.AliasFlag aliasFlag,
        Object proxyModel,
        List<? extends Mapping<?, ?, ?>> mappedItemList
    ) {
        List<SelectNode> selectNodeList = new ArrayList<>();

        for (Mapping<?, ?, ?> mapping : mappedItemList) {
            SingleValueElementDesc<?, ?> singleValueElementDesc = mapping.getDesc();

            // 解析成MapDefinition
            MapDefinition mapDefinition = this.parseMapDefinition(toolbox, proxyModel, aliasFlag, singleValueElementDesc);

            // 保存到结果list中
            selectNodeList.add(new CommonSelectNode(mapDefinition.getAliasFlag(), mapDefinition));

        }

        return selectNodeList;
    }

    private MapDefinition parseMapDefinition(SqlParserPortableToolbox toolbox, Object proxyModel, SQLAliasManager.AliasFlag aliasFlag, SingleValueElementDesc<?, ?> singleValueElementDesc) {
        SqlParser sqlParser = toolbox.getSqlPreParser();
        if (singleValueElementDesc instanceof FieldMapping) {
            // 字段类的select
            MapDefinition mapDefinition = this.getFieldDefinitionFromThreadLocal((FieldMapping<?, ?>) singleValueElementDesc, proxyModel);
            // 会多一层，丢弃掉
            return (MapDefinition) mapDefinition.getValueDefinition();
        } else if (singleValueElementDesc instanceof SqlQueryMetaDesc) {
            // 子查询类型的select
            sqlParser.parseByDesc((WholeDesc) singleValueElementDesc, toolbox);
            QueryProxyNode subQueryProxyNode = toolbox.popQueryProxyNode();
            return new MapDefinition(
                subQueryProxyNode.getQuerySelectMeta(),
                null,
                subQueryProxyNode.getAliasFlag()
            );

        } else if (singleValueElementDesc instanceof Param) {
            // 参数类型的select
            ParamReceiptManager paramReceiptManager = toolbox.getParamReceiptManager();
            ParamReceipt paramReceipt = paramReceiptManager.registerParam((Param<?>) singleValueElementDesc);
            return new MapDefinition(
                paramReceipt,
                null,
                SQLAliasManager.createAlias()
            );

//            } else if (singleValueElementDesc instanceof MFunctionDesc) {
//            return mapDefinition;
            // TODO

        } else {
            throw UnbelievableException.unknownType();
        }
    }

    public MapDefinition getFieldDefinitionFromThreadLocal(FieldMapping<?, ?> fieldMapping, Object proxyModel) {
        ((FieldMapping<Object, Object>) fieldMapping).getMapping(proxyModel);
        return THREAD_LOCAL.get();
    }

    private Assigner<ResultSet> parseAssignerByMappedItemList(Class<?> selectModelClass, List<? extends Mapping<?, ?, ?>> mappedItemList) {
        List<SetMapping<Object, Object>> mappingList = mappedItemList.stream()
            .map(mapping -> (SetMapping<Object, Object>) mapping.getMappingField())
            .collect(Collectors.toList());
        return new SQLMetaModelResultAssigner(selectModelClass, mappingList);
    }

    @Override
    public QueryFromMeta getQueryFromMeta() {
        return this.sqlQueryDefinition;
    }

    @Override
    public Object getProxyModel() {
        return this.proxyModel;
    }

    public List<FromNode> getFromNodeList() {
        return fromNodeList;
    }

    public List<SelectNode> getSelectNodeList() {
        return selectNodeList;
    }

    @Override
    public SQLAliasManager.AliasFlag getAliasFlag() {
        return this.aliasFlag;
    }

    @Override
    public SqlQueryDefinition getQuerySelectMeta() {
        return this.sqlQueryDefinition;
    }

    public Assigner<ResultSet> getAssigner() {
        return assigner;
    }
}
