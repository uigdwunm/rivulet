package zly.rivulet.sql.parser.proxy_node;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.CollectionUtils;
import zly.rivulet.base.utils.PairReturn;
import zly.rivulet.sql.SQLRivuletManager;
import zly.rivulet.sql.assigner.SQLContainerResultAssigner;
import zly.rivulet.sql.assigner.SQLMetaModelResultAssigner;
import zly.rivulet.sql.assigner.SQLOneResultAssigner;
import zly.rivulet.sql.assigner.SQLQueryResultAssigner;
import zly.rivulet.sql.definer.SQLDefiner;
import zly.rivulet.sql.definer.annotations.SQLSubQuery;
import zly.rivulet.sql.definer.annotations.SQLQueryAlias;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.query.SQLQueryDefinition;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.describer.join.QueryComplexModel;
import zly.rivulet.sql.describer.query.SQLQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.SQLParser;
import zly.rivulet.sql.parser.toolbox.SQLParserPortableToolbox;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

public class QueryProxyNode implements SelectNode, FromNode {

    /**
     * 是SqlQueryDefinition, 解析时没啥用,也不能用,提前放到这
     **/
    private final SQLQueryDefinition sqlQueryDefinition;

    private final Object proxyModel;

    /**
     * 当前query的所有from，如果是单表查询则只有一个，但是不会为空
     **/
    private final List<FromNode> fromNodeList;

    /**
     * 当前query的所有select
     **/
    private final List<SelectNode> selectNodeList;

    /**
     * where条件中如果出现子查询也记录到这里
     **/
    private final List<QueryProxyNode> conditionSubQueryList = new ArrayList<>();

    private final Assigner<ResultSet> assigner;
    /**
     * 当前节点的别名flag
     **/
    private final SQLAliasManager.AliasFlag aliasFlag = SQLAliasManager.createAlias();


    /**
     * Description queryDesc形式的解析
     *
     * @author zhaolaiyuan
     * Date 2022/10/24 8:27
     **/
    QueryProxyNode(SQLQueryDefinition sqlQueryDefinition, SQLParserPortableToolbox toolbox, SQLQueryMetaDesc<?, ?> sqlQueryMetaDesc) {
        this.sqlQueryDefinition = sqlQueryDefinition;
        Class<?> fromModelClass = sqlQueryMetaDesc.getMainFrom();
        Class<?> selectModelClass = sqlQueryMetaDesc.getSelectModel();
        List<? extends Mapping<?, ?, ?>> mappedItemList = sqlQueryMetaDesc.getMappedItemList();
        ConvertorManager convertorManager = toolbox.getSqlPreParser().getConvertorManager();
        // 先把当前的node塞进去，下面会用到
        toolbox.setQueryProxyNode(this);

        // 根据from模型和select方式的不同，有多种解析路径
        if (ClassUtils.isExtend(QueryComplexModel.class, fromModelClass)) {
            // from是复杂查询模型
            // 复杂查询模型还需要填充node和field的映射，解析select可能会用到
            Map<FromNode, Field> fromNodeFieldMap = new HashMap<>();
            this.proxyModel = this.createProxyByComplexModel(fromModelClass, aliasFlag);
            this.fromNodeList = this.parseFromNodeListByComplexModel(toolbox, proxyModel, fromModelClass, fromNodeFieldMap);

            if (CollectionUtils.isNotEmpty(mappedItemList)) {
                // 预先指定的结果映射
                // 通过指定好的映射解析
                selectNodeList = this.parseSelectNodeListByMappedItemList(toolbox, proxyModel, mappedItemList);
                // 生成赋值器
                if (sqlQueryMetaDesc.isOneResult()) {
                    assigner = new SQLOneResultAssigner(convertorManager, selectModelClass);
                } else {
                    assigner = this.parseAssignerByMappedItemList(convertorManager, selectModelClass, mappedItemList);
                }
            } else if (fromModelClass.equals(selectModelClass)) {
                PairReturn<List<SelectNode>, SQLQueryResultAssigner> pairReturn = this.parseSelectNodeListAndAssignerByComplexModel(convertorManager, fromNodeList, selectModelClass, fromNodeFieldMap);
                selectNodeList = pairReturn.getLeft();
                assigner = pairReturn.getRight();
            } else {
                // 两种情况都不是，抛异常
                throw SQLDescDefineException.selectAndFromNoMatch();
            }
        } else {
            // from是表模型
            // 生成node
            SQLParser sqlParser = toolbox.getSqlPreParser();
            ProxyNodeManager proxyModelManager = sqlParser.getProxyModelManager();
            SQLModelMeta sqlModelMeta = sqlParser.getDefiner().createOrGetModelMeta(fromModelClass);
            MetaModelProxyNode metaModelProxyNode = proxyModelManager.getOrCreateProxyMetaModel(sqlModelMeta, toolbox);

            this.proxyModel = metaModelProxyNode.getProxyModel();
            this.fromNodeList = Collections.singletonList(metaModelProxyNode);

            if (CollectionUtils.isNotEmpty(mappedItemList)) {
                // 预先指定的结果映射
                // 通过指定好的映射解析
                selectNodeList = this.parseSelectNodeListByMappedItemList(toolbox, proxyModel, mappedItemList);
                // 生成赋值器
                if (sqlQueryMetaDesc.isOneResult()) {
                    assigner = new SQLOneResultAssigner(convertorManager, selectModelClass);
                } else {
                    assigner = this.parseAssignerByMappedItemList(convertorManager, selectModelClass, mappedItemList);
                }
            } else if (fromModelClass.equals(selectModelClass)) {
                PairReturn<List<SelectNode>, SQLQueryResultAssigner> pairReturn = this.parseSelectNodeListAndAssignerByMetaModel(convertorManager, metaModelProxyNode);
                selectNodeList = pairReturn.getLeft();
                assigner = pairReturn.getRight();
            } else {
                // 两种情况都不是，抛异常
                throw SQLDescDefineException.selectAndFromNoMatch();
            }
        }
    }

    /**
     * Description 以MetaModel模型解析
     *
     * @author zhaolaiyuan
     * Date 2022/10/16 11:37
     **/
    QueryProxyNode(SQLParserPortableToolbox toolbox, SQLModelMeta sqlModelMeta) {
        // 给meta模型通用的，不只是query
        this.sqlQueryDefinition = null;

        // from是表模型
        SQLParser sqlPreParser = toolbox.getSqlPreParser();
        ProxyNodeManager proxyModelManager = sqlPreParser.getProxyModelManager();
        ConvertorManager convertorManager = sqlPreParser.getConvertorManager();
        MetaModelProxyNode metaModelProxyNode = proxyModelManager.getOrCreateProxyMetaModel(sqlModelMeta, toolbox);
        this.proxyModel = metaModelProxyNode.getProxyModel();
        this.fromNodeList = Collections.singletonList(metaModelProxyNode);

        PairReturn<List<SelectNode>, SQLQueryResultAssigner> pairReturn = this.parseSelectNodeListAndAssignerByMetaModel(convertorManager, metaModelProxyNode);
        selectNodeList = pairReturn.getLeft();
        assigner = pairReturn.getRight();
    }

    /**
     * Description update的desc解析
     *
     * @author zhaolaiyuan
     * Date 2022/10/24 8:36
     **/
    QueryProxyNode(SQLParserPortableToolbox toolbox, Class<?> fromModelClass) {
        // from只能是表模型
        // 生成node
        SQLParser sqlParser = toolbox.getSqlPreParser();
        ProxyNodeManager proxyModelManager = sqlParser.getProxyModelManager();
        SQLModelMeta sqlModelMeta = sqlParser.getDefiner().createOrGetModelMeta(fromModelClass);
        MetaModelProxyNode metaModelProxyNode = proxyModelManager.getOrCreateProxyMetaModel(sqlModelMeta, toolbox);

        this.proxyModel = metaModelProxyNode.getProxyModel();
        this.fromNodeList = Collections.singletonList(metaModelProxyNode);

        // 没有select不需要
        this.sqlQueryDefinition = null;
        selectNodeList = null;
        assigner = null;
    }

    private PairReturn<List<SelectNode>, SQLQueryResultAssigner> parseSelectNodeListAndAssignerByComplexModel(
        ConvertorManager convertorManager,
        List<FromNode> fromNodeList,
        Class<?> selectModelClass,
        Map<FromNode, Field> fromNodeFieldMap
    ) {
        List<SelectNode> selectNodeList = new ArrayList<>();
        List<SQLContainerResultAssigner.ContainerFieldAssignerWrap> assignerList = new ArrayList<>();
        for (FromNode fromNode : fromNodeList) {
            Field field = fromNodeFieldMap.get(fromNode);
            Assigner<ResultSet> fromItemAssigner;
            if (fromNode instanceof MetaModelProxyNode) {
                MetaModelProxyNode metaModelProxyNode = (MetaModelProxyNode) fromNode;
                PairReturn<List<SelectNode>, SQLQueryResultAssigner> pairReturn = this.parseSelectNodeListAndAssignerByMetaModel(convertorManager, metaModelProxyNode);
                selectNodeList.addAll(pairReturn.getLeft());
                // 生成模型的赋值器
                fromItemAssigner = pairReturn.getRight();
            } else if (fromNode instanceof QueryProxyNode) {
                QueryProxyNode queryProxyNode = (QueryProxyNode) fromNode;
                SQLQueryDefinition sqlQueryDefinition = queryProxyNode.getQuerySelectMeta();
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

    private PairReturn<List<SelectNode>, SQLQueryResultAssigner> parseSelectNodeListAndAssignerByMetaModel(ConvertorManager convertorManager, MetaModelProxyNode metaModelProxyNode) {
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
        SQLMetaModelResultAssigner assigner = new SQLMetaModelResultAssigner(convertorManager, queryFromMeta.getModelClass(), setMappingList);
        return PairReturn.of(selectNodeList, assigner);
    }

    private List<FromNode> parseFromNodeListByComplexModel(
        SQLParserPortableToolbox toolbox,
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
            SQLQueryAlias sqlQueryAlias = field.getAnnotation(SQLQueryAlias.class);
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
            field.setAccessible(true);
            try {
                field.set(proxyModel, fromNode.getProxyModel());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return fromNodeList;
    }

    private FromNode parseFromNode(SQLParserPortableToolbox toolbox, Field field) {
        SQLParser sqlParser = toolbox.getSqlPreParser();
        Class<?> fieldType = field.getType();
        // 这个注解表示当前字段代表一个子查询，value是key
        SQLSubQuery sqlSubQuery = field.getAnnotation(SQLSubQuery.class);
        if (sqlSubQuery != null) {
            SQLRivuletManager sqlRivuletManager = sqlParser.getSqlRivuletManager();
            sqlRivuletManager.getWholeDescByDescKey(sqlSubQuery.value());
            // 解析子查询
            WholeDesc wholeDesc = sqlRivuletManager.getWholeDescByDescKey(sqlSubQuery.value());
            // 校验下
            if (wholeDesc instanceof SQLQueryMetaDesc) {
                SQLQueryMetaDesc<Object, Object> sqlQueryMetaDesc = (SQLQueryMetaDesc<Object, Object>) wholeDesc;
                // 校验子查询的模型是否等于原desc的from模型
                if (!fieldType.equals(sqlQueryMetaDesc.getMainFrom())) {
                    throw SQLDescDefineException.subQueryMustOriginFrom(sqlSubQuery.value(), fieldType, sqlQueryMetaDesc.getMainFrom());
                }
                if (!sqlQueryMetaDesc.getMainFrom().equals(sqlQueryMetaDesc.getSelectModel())) {
                    // 如果原desa的from模型和select模型不匹配，则重新改造一个
                    Class<Object> mainFrom = (Class<Object>) sqlQueryMetaDesc.getMainFrom();
                    wholeDesc = new SQLQueryMetaDesc<>(
                        mainFrom,
                        mainFrom,
                        false,
                        null,
                        sqlQueryMetaDesc.getWhereConditionContainer(),
                        sqlQueryMetaDesc.getGroupFieldList(),
                        sqlQueryMetaDesc.getHavingConditionContainer(),
                        sqlQueryMetaDesc.getOrderItemList(),
                        sqlQueryMetaDesc.getSkit(),
                        sqlQueryMetaDesc.getLimit(),
                        sqlQueryMetaDesc.getCustomStatementMap()
                    );
                }
            } else {
                throw SQLDescDefineException.mustQueryKey(sqlSubQuery.value(), fieldType);
            }
            // 解析子查询
            sqlParser.parse(wholeDesc, toolbox);
            return toolbox.popQueryProxyNode();
        } else {
            SQLDefiner sqlDefiner = sqlParser.getDefiner();
            // 不是子查询, 则按modelMeta解析
            SQLModelMeta sqlModelMeta = sqlDefiner.createOrGetModelMeta(field.getType());
            if (sqlModelMeta == null) {
                // 没找到对应的表对象
                throw SQLDescDefineException.unknownQueryType(field.getType());
            }
            ProxyNodeManager proxyModelManager = sqlParser.getProxyModelManager();
            return proxyModelManager.getOrCreateProxyMetaModel(sqlModelMeta, toolbox);
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
        SQLParserPortableToolbox toolbox,
        Object proxyModel,
        List<? extends Mapping<?, ?, ?>> mappedItemList
    ) {
        List<SelectNode> selectNodeList = new ArrayList<>();

        for (Mapping<?, ?, ?> mapping : mappedItemList) {
            SingleValueElementDesc<?, ?> singleValueElementDesc = mapping.getDesc();

            // 解析成MapDefinition
            MapDefinition mapDefinition = toolbox.parseSingValueForSelect(proxyModel, singleValueElementDesc);

            // 保存到结果list中
            selectNodeList.add(new CommonSelectNode(mapDefinition.getAliasFlag(), mapDefinition));

        }

        return selectNodeList;
    }

    public MapDefinition getFieldDefinitionFromThreadLocal(FieldMapping<?, ?> fieldMapping, Object proxyModel) {
        ((FieldMapping<Object, Object>) fieldMapping).getMapping(proxyModel);
        // 多了一层，丢弃掉
//        return (MapDefinition) THREAD_LOCAL.get().getValueDefinition();
        return THREAD_LOCAL.get();
    }

    public MapDefinition getFieldDefinitionFromThreadLocal(JoinFieldMapping<?> fieldMapping, Object proxyModel) {
        ((JoinFieldMapping<Object>) fieldMapping).getMapping();
        // 多了一层，丢弃掉
//        return (MapDefinition) THREAD_LOCAL.get().getValueDefinition();
        return THREAD_LOCAL.get();
    }

    private SQLQueryResultAssigner parseAssignerByMappedItemList(ConvertorManager convertorManager, Class<?> selectModelClass, List<? extends Mapping<?, ?, ?>> mappedItemList) {
        List<SetMapping<Object, Object>> mappingList = mappedItemList.stream()
            .map(mapping -> (SetMapping<Object, Object>) mapping.getMappingField())
            .collect(Collectors.toList());
        return new SQLMetaModelResultAssigner(convertorManager, selectModelClass, mappingList);
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

    public void addConditionSubQuery(QueryProxyNode queryProxyNode) {
        this.conditionSubQueryList.add(queryProxyNode);
    }

    public List<QueryProxyNode> getConditionSubQueryList() {
        return conditionSubQueryList;
    }

    @Override
    public SQLAliasManager.AliasFlag getAliasFlag() {
        return this.aliasFlag;
    }

    @Override
    public SQLQueryDefinition getQuerySelectMeta() {
        return this.sqlQueryDefinition;
    }

    public Assigner<ResultSet> getAssigner() {
        return assigner;
    }

    public FromNode getFromNode(Object proxyModel) {
        // 目前看来这个不怎么常用，没必要专门弄个map
        for (FromNode fromNode : fromNodeList) {
            if (fromNode.getProxyModel().equals(proxyModel)) {
                return fromNode;
            }
        }
        return null;
    }
}
