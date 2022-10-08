package zly.rivulet.sql.parser.proxy_node;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.CollectionUtils;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definer.annotations.SQLSubQuery;
import zly.rivulet.sql.definer.annotations.SqlQueryAlias;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.describer.function.MFunctionDesc;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.SqlParser;
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

    /**
     * 专用于DOModel智能update保存的proxyNode
     **/
    private final Map<SQLModelMeta, ModelProxyNode> forUpdateProxyNodeMap = new ConcurrentHashMap<>();

    /**
     * Description 缓存起来，用于解析运行时手动创建的Definition （比如运行是手动指定sort部分definition）
     *
     * @author zhaolaiyuan
     * Date 2022/10/8 19:46
     **/
//    private final Map<SqlQueryDefinition, QueryProxyNode> queryProxyNodeMap = new ConcurrentHashMap<>();

    private final SqlParser sqlParser;

    private final SqlDefiner sqlDefiner;

    public ProxyNodeManager(SqlParser sqlParser) {
        this.sqlParser = sqlParser;
        this.sqlDefiner = sqlParser.getDefiner();
    }

    public QueryProxyNode parseProxyNode(SqlQueryDefinition sqlQueryDefinition, SqlParserPortableToolbox toolbox, SqlQueryMetaDesc<?, ?> sqlQueryMetaDesc) {
        Class<?> fromModelClass = sqlQueryMetaDesc.getMainFrom();

        // 解析from模型
        Object proxyModel;
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
        if (CollectionUtils.isEmpty(mappedItemList)) {
            // 通过指定好的映射解析
            selectNodeList = this.parseMappedItemListSelect(proxyModel, fromNodeList, mappedItemList);
        } else if (fromModelClass.equals(selectModelClass)) {
            // 直接解析模型为select
            selectNodeList = this.parseModelSelect(fromNodeList, selectModelClass);
        } else {
            // 两种情况都不是，抛异常
            throw SQLDescDefineException.selectAndFromNoMatch();
        }

        return new QueryProxyNode(proxyModel, sqlQueryDefinition, fromNodeList, selectNodeList);
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
            sqlParser.parseByKey(sqlSubQuery.value(), toolbox);
            return toolbox.popQueryProxyNode();
        } else {
            // 不是子查询, 则按modelMeta解析
            SQLModelMeta sqlModelMeta = sqlDefiner.createOrGetModelMeta(field.getType());
            if (sqlModelMeta == null) {
                // 没找到对应的表对象
                throw SQLDescDefineException.unknowQueryType();
            }
            return this.createMetaModel(sqlModelMeta);
        }
    }

    private ModelProxyNode parseMetaModelFrom(Class<?> fromModelClass) {
        SQLModelMeta sqlModelMeta = sqlDefiner.createOrGetModelMeta(fromModelClass);
        return this.createMetaModel(sqlModelMeta);
    }

    private List<SelectNode> parseMappedItemListSelect(Object proxyModel, List<FromNode> fromNodeList, List<? extends Mapping<?, ?, ?>> mappedItemList) {
        // TODO
        for (Mapping<?, ?, ?> mapping : mappedItemList) {
            SetMapping<?, ?> mappingField = mapping.getMappingField();
            SingleValueElementDesc<?, ?> singleValueElementDesc = mapping.getDesc();
            if (singleValueElementDesc instanceof FieldMapping) {
                FieldDefinition fieldDefinition = this.getFieldDefinitionFromThreadLocal((FieldMapping<?, ?>) singleValueElementDesc, proxyModel);
                // TODO 有问题，这个有可能是从很深很深的子查询中拿到的，路径都无法得到

            } else if (singleValueElementDesc instanceof SqlQueryMetaDesc) {

            } else if (singleValueElementDesc instanceof Param) {


            } else if (singleValueElementDesc instanceof MFunctionDesc) {


            } else {

            }

        }
        return null;
    }

    private List<SelectNode> parseModelSelect(List<FromNode> fromNodeList, Class<?> selectModelClass) {
        // TODO
        return null;
    }

    /**
     * Description 获取缓存的modelProxyNode，专门用于ModelUpdate
     *
     * @author zhaolaiyuan
     * Date 2022/10/8 19:42
     **/
    public ModelProxyNode getOrCreateProxyMetaModelForModelUpdate(SQLModelMeta sqlModelMeta) {
        ModelProxyNode modelProxyNode = forUpdateProxyNodeMap.get(sqlModelMeta);
        if (modelProxyNode == null) {
            return modelProxyNode;
        }

        modelProxyNode = this.createMetaModel(sqlModelMeta);
        forUpdateProxyNodeMap.put(sqlModelMeta, modelProxyNode);
        return modelProxyNode;
    }

    private ModelProxyNode createMetaModel(SQLModelMeta sqlModelMeta) {
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
        return new ModelProxyNode(aliasFlag, sqlModelMeta, enhancer.create());
    }

    public FieldDefinition getFieldDefinitionFromThreadLocal(FieldMapping<?, ?> fieldMapping, Object proxyModel) {
        ((FieldMapping<Object, Object>) fieldMapping).getMapping(proxyModel);
        return THREAD_LOCAL.get();
    }
}
