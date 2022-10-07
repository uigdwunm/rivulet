package zly.rivulet.sql.parser.proxy_node;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.CollectionUtils;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definer.annotations.SQLSubQuery;
import zly.rivulet.sql.definer.annotations.SqlQueryAlias;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.SQLBlueprint;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.SqlParser;
import zly.rivulet.sql.parser.proxy_node.FromNode;
import zly.rivulet.sql.parser.proxy_node.ModelProxyNode;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ProxyNodeManager {
    private static final ThreadLocal<FieldDefinition> THREAD_LOCAL = new ThreadLocal<>();

    private final Map<SQLModelMeta, ModelProxyNode> proxyNodeMap = new ConcurrentHashMap<>();

    private final Map<SqlQueryDefinition, QueryProxyNode> queryProxyNodeMap = new ConcurrentHashMap<>();

    private final SqlParser sqlParser;

    private final SqlDefiner sqlDefiner;

    public ProxyNodeManager(SqlParser sqlParser) {
        this.sqlParser = sqlParser;
        this.sqlDefiner = sqlParser.getDefiner();
    }

    public QueryProxyNode parseProxyNode(SqlQueryDefinition sqlQueryDefinition, SqlParserPortableToolbox toolbox, SqlQueryMetaDesc<?, ?> sqlQueryMetaDesc) {
        Class<?> fromModelClass = sqlQueryMetaDesc.getMainFrom();

        Object proxyModel;
        // 解析from模型
        List<FromNode> fromNodeList;
        if (ClassUtils.isExtend(QueryComplexModel.class, fromModelClass)) {
            proxyModel = ClassUtils.newInstance(fromModelClass);
            fromNodeList = this.parseComplexModelFrom(proxyModel, fromModelClass, toolbox);
        } else {
            ModelProxyNode modelProxyNode = this.parseMetaModelFrom(fromModelClass);
            proxyModel = modelProxyNode.getProxyModel();
            fromNodeList = Collections.singletonList(modelProxyNode);
        }

        // 解析select模型
        List<SelectNode> selectNodeList;
        Class<?> selectModelClass = sqlQueryMetaDesc.getSelectModel();
        List<? extends Mapping<?, ?, ?>> mappedItemList = sqlQueryMetaDesc.getMappedItemList();
        // 解析select模型
        if (CollectionUtils.isEmpty(mappedItemList)) {
            // 通过指定好的映射
            selectNodeList = this.parseMappedItemListSelect(fromNodeList, mappedItemList);
        } else if (fromModelClass.equals(selectModelClass)) {
            selectNodeList = this.parseModelSelect(fromNodeList, selectModelClass);
        } else {
            throw SQLDescDefineException.selectAndFromNoMatch();
        }

        QueryProxyNode queryProxyNode = new QueryProxyNode(proxyModel, sqlQueryDefinition, fromNodeList, selectNodeList);
        this.queryProxyNodeMap.put(sqlQueryDefinition, queryProxyNode);
        return queryProxyNode;
    }

    private List<FromNode> parseComplexModelFrom(Object proxyModel, Class<?> fromModelClass, SqlParserPortableToolbox toolbox) {
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
        // 这个注解表示当前字段代表一个子查询，value是key
        SQLSubQuery sqlSubQuery = field.getAnnotation(SQLSubQuery.class);
        if (sqlSubQuery != null) {
            // 解析子查询
            SqlQueryDefinition subQueryDefinition = (SqlQueryDefinition) sqlParser.parseByKey(sqlSubQuery.value(), toolbox);
            return this.queryProxyNodeMap.get(subQueryDefinition);
        } else {
            // 不是子查询, 则按modelMeta解析
            SQLModelMeta sqlModelMeta = sqlDefiner.createOrGetModelMeta(field.getType());
            if (sqlModelMeta == null) {
                // 没找到对应的表对象
                throw SQLDescDefineException.unknowQueryType();
            }
            return this.getOrCreateMetaModel(sqlModelMeta);
        }
    }

    private ModelProxyNode parseMetaModelFrom(Class<?> fromModelClass) {
        SQLModelMeta sqlModelMeta = sqlDefiner.createOrGetModelMeta(fromModelClass);
        return this.getOrCreateMetaModel(sqlModelMeta);
    }

    private List<SelectNode> parseMappedItemListSelect(List<FromNode> fromNodeList, List<? extends Mapping<?, ?, ?>> mappedItemList) {
        // TODO
        return null;
    }

    private List<SelectNode> parseModelSelect(List<FromNode> fromNodeList, Class<?> selectModelClass) {
        // TODO
        return null;
    }

    private ModelProxyNode getOrCreateMetaModel(SQLModelMeta sqlModelMeta) {
        ModelProxyNode modelProxyNode = proxyNodeMap.get(sqlModelMeta);
        if (modelProxyNode == null) {
            return modelProxyNode;
        }

        String firstChar = String.valueOf(sqlModelMeta.getTableName().charAt(0));
        SQLAliasManager.AliasFlag aliasFlag = SQLAliasManager.createAlias(firstChar);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(sqlModelMeta.getModelClass());
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                // 执行
                Object result = methodProxy.invokeSuper(o, args);

                // 通过方法名解析出字段，获取fieldMeta
                String methodName = method.getName();
                if (!StringUtil.checkGetterMethodName(methodName)) {
                    // 只能解析get开头的方法
                    return result;
                }


                FieldDefinition fieldDefinition = THREAD_LOCAL.get();
                if (fieldDefinition == null) {
                    String fieldName = StringUtil.parseGetterMethodNameToFieldName(methodName);
                    THREAD_LOCAL.set(new FieldDefinition(sqlModelMeta, fieldName, aliasFlag));
                }
                return result;
            }
        });
        modelProxyNode = new ModelProxyNode(aliasFlag, sqlModelMeta, enhancer.create());
        proxyNodeMap.put(sqlModelMeta, modelProxyNode);
        return modelProxyNode;
    }

    public FieldDefinition getFieldDefinitionFromThreadLocal() {
        return THREAD_LOCAL.get();
    }
}
