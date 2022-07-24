package zly.rivulet.sql.preparser.helper.node;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.SelectMapping;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definer.annotations.SQLModelJoin;
import zly.rivulet.sql.definer.annotations.SQLSubJoin;
import zly.rivulet.sql.definer.annotations.SqlQueryAlias;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.definition.query.main.FromDefinition;
import zly.rivulet.sql.definition.query.main.SelectDefinition;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.preparser.SqlPreParser;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class QueryProxyNode implements FromNode, SelectNode {
    ThreadLocal<SQLSingleValueElementDefinition> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 当前query的所有select
     **/
    private List<SelectNode> selectNodeList = new ArrayList<>();

    /**
     * 当前query的所有from，如果是单表查询则只有一个，但是不会为空
     **/
    private final List<FromNode> fromNodeList = new ArrayList<>();

    private final Map<FromNode, Field> fromNode_field_map = new HashMap<>();

    /**
     * Description 代理对象与node的映射，考虑删掉
     *
     * Date 2022/7/3 16:37
     **/
    private final Map<Object, FromNode> proxy_fromNode_map = new HashMap<>();

    /**
     * where条件中如果出现子查询也记录到这里
     **/
    private List<QueryProxyNode> conditionSubQueryList = new ArrayList<>();

    /**
     * 当前节点所属的父节点,如果是根节点则为null
     **/
    private QueryProxyNode parentNode;

    /**
     * 当前对象的代理对象，联表查询的代理对象，或者单表查询的表对象
     *
     **/
    private Object proxyModel;

    private final Class<?> fromModelClass;

    private final SqlPreParseHelper sqlPreParseHelper;

    /**
     * 当前节点的别名flag
     **/
    private final SQLAliasManager.AliasFlag aliasFlag = SQLAliasManager.createModelAlias();

    /**
     * 整个definition解析完还会把结果塞到这里
     **/
    private SqlQueryDefinition sqlQueryDefinition;

    /**
     * 结果对象class
     **/
    private final Class<?> selectModelClass;

    private final List<MapDefinition> mapDefinitionList = new ArrayList<>();

    /**
     * 结果对象字段名和MappingDefinition的映射，vo结果并且子查询时才有
     **/
    private final Map<String, MapDefinition> mappingDefinitionMap = new HashMap<>();

    public QueryProxyNode(SqlPreParseHelper sqlPreParseHelper, SqlQueryMetaDesc<?, ?> metaDesc) {
        Class<?> mainFrom = metaDesc.getMainFrom();
        this.fromModelClass = mainFrom;
        this.selectModelClass = metaDesc.getSelectModel();
        this.sqlPreParseHelper = sqlPreParseHelper;
        if (QueryComplexModel.class.isAssignableFrom(mainFrom)) {
            this.registerComplexProxyModel(mainFrom);
        } else {
            SqlDefiner sqlDefiner = sqlPreParseHelper.getSqlPreParser().getSqlDefiner();
            SQLModelMeta modelMeta = sqlDefiner.createOrGetModelMeta(mainFrom);
            SQLAliasManager.AliasFlag modelAlias = SQLAliasManager.createModelAlias(modelMeta.getTableName(), modelMeta);
            // 生成from代理节点
            ModelProxyNode modelProxyNode = registerFromModel(modelAlias, modelMeta);
            this.proxyModel = modelProxyNode.getProxyModel();
        }
    }

    public void registerComplexProxyModel(Class<?> clazz) {
        SqlPreParser sqlPreParser = sqlPreParseHelper.getSqlPreParser();
        SqlDefiner sqlDefiner = sqlPreParser.getSqlDefiner();
        Object o = this.proxyDONewInstance(clazz);
        // 每个字段注入代理对象
        for (Field field : clazz.getDeclaredFields()) {
            SQLModelJoin sqlModelJoin = field.getAnnotation(SQLModelJoin.class);
            SQLSubJoin sqlSubJoin = field.getAnnotation(SQLSubJoin.class);
            if ((sqlModelJoin != null && sqlSubJoin != null) || (sqlModelJoin == null && sqlSubJoin == null)) {
                // 两个注解都有，或两个注解都没有，报错
                throw SQLDescDefineException.unknowQueryType();
            }
            // 别名
            SqlQueryAlias sqlQueryAlias = field.getAnnotation(SqlQueryAlias.class);
            FromNode fromNode;

            if (sqlModelJoin != null) {
                SQLModelMeta modelMeta = sqlDefiner.createOrGetModelMeta(field.getType());
                if (modelMeta == null) {
                    // 没找到对应的表对象
                    throw SQLDescDefineException.unknowQueryType();
                }
                SQLAliasManager.AliasFlag modelAlias;
                if (sqlQueryAlias != null) {
                    modelAlias = SQLAliasManager.createModelAlias(sqlQueryAlias, modelMeta);
                } else {
                    modelAlias = SQLAliasManager.createModelAlias(field.getName(), modelMeta);
                }
                fromNode = registerFromModel(modelAlias, modelMeta);

                // 注入外层代理对象field
                this.proxyDOFieldSetValue(field, o, fromNode.getProxyModel());
//            } else if (sqlSubJoin != null) {
            } else {
                // 保留当前的外层node节点
                QueryProxyNode currNode = sqlPreParseHelper.getCurrNode();
                FinalDefinition finalDefinition = sqlPreParser.parse(sqlSubJoin.value(), sqlPreParseHelper);
                SqlQueryDefinition subQueryDefinition = (SqlQueryDefinition) finalDefinition;
                // 上面的解析过程会把curr替换成子查询对应的node
                QueryProxyNode subNode = sqlPreParseHelper.getCurrNode();
                subNode.sqlQueryDefinition = subQueryDefinition;

                this.acceptSubQueryProxyModel(subNode, subQueryDefinition, sqlQueryAlias, field.getName());
                // 这里替换回来
                sqlPreParseHelper.setCurrNode(currNode);

                // 子查询解析的node就是from节点
                fromNode = subNode;

                // 注入代理对象
                this.proxyDOFieldSetValue(field, o, fromNode.getProxyModel());

                // 加到from集合中
                this.fromNodeList.add(subNode);
                this.proxy_fromNode_map.put(subNode.getProxyModel(), subNode);
            }

            this.fromNode_field_map.put(fromNode, field);
        }

        this.proxyModel = o;
    }

    /**
     * Description 构建子查询的代理对象，因为子查询可能用的是vo，所以要麻烦些
     *
     * @author zhaolaiyuan
     * Date 2022/6/25 11:32
     **/
    private void acceptSubQueryProxyModel(QueryProxyNode subNode, SqlQueryDefinition subQueryDefinition, SqlQueryAlias sqlQueryAlias, String suggestedAlias) {
        //
        // 为子节点关联父节点
        subNode.parentNode = this;
        // 为子结点弄上别名
        if (sqlQueryAlias != null) {
            subNode.aliasFlag.forceAlias(sqlQueryAlias.value());
        } else {
            subNode.aliasFlag.suggestedAlias(suggestedAlias);
        }
        SelectDefinition selectDefinition = subQueryDefinition.getSelectDefinition();
        FromDefinition fromDefinition = subQueryDefinition.getFromDefinition();
        // 先判断 from对象和结果对象是不是同一个
        if (fromDefinition.getFromMode().equals(selectDefinition.getSelectModel())) {
            // 结果对象和from对象是同一个,那么可以直接返回fromModel对应的代理对象
            return;
        }
        subNode.proxyModel = this.createProxyVO(subQueryDefinition);

        // 执行一遍vo的映射方法，生成vo字段名与映射definition的对应关系
        View<MapDefinition> subMappingDefinitionList = subQueryDefinition.getSelectDefinition().getMapDefinitionList();
        for (MapDefinition mapDefinition : subMappingDefinitionList) {
            SelectMapping<Object, ?> selectField = (SelectMapping<Object, ?>) mapDefinition.getSelectField();
            String fieldName = parseFieldName(selectField);
            mappingDefinitionMap.put(fieldName, mapDefinition);
        }

    }

    private String parseFieldName(SelectMapping<Object, ?> selectField) {
        try {
            Method method = selectField.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda lambda = (SerializedLambda) method.invoke(selectField);
            String methodName = lambda.getImplMethodName();
            char[] methodNameArr = methodName.toCharArray();
            char[] fieldNameArr = new char[methodNameArr.length - 3];
            methodNameArr[3] = (char) (methodNameArr[3] + 32);

            System.arraycopy(methodNameArr, 3, fieldNameArr, 0, methodNameArr.length - 3);
            return new String(fieldNameArr);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Object createProxyVO(SqlQueryDefinition subQueryDefinition) {
        Class<?> resultModelClass = subQueryDefinition.getSelectDefinition().getSelectModel();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(resultModelClass);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                // 执行
                Object result = methodProxy.invokeSuper(o, args);

                // 通过方法名解析出字段，获取fieldMeta
                String methodName = method.getName();
                if (!methodName.startsWith("get")) {
                    // 只能解析get开头的方法，不解析is方法
                    return result;
                }

                char[] methodNameArr = methodName.toCharArray();
                char[] fieldNameArr = new char[methodNameArr.length - 3];
                methodNameArr[3] = (char) (methodNameArr[3] + 32);

                System.arraycopy(methodNameArr, 3, fieldNameArr, 0, methodNameArr.length - 3);
                String fieldName = new String(fieldNameArr);

                MapDefinition mapDefinition = mappingDefinitionMap.get(fieldName);
                THREAD_LOCAL.set(mapDefinition);
                return result;
            }
        });
        return enhancer.create();
    }

    public ModelProxyNode registerFromModel(SQLAliasManager.AliasFlag modelAlias, SQLModelMeta modelMeta) {
        Class<?> clazz = modelMeta.getModelClass();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                // 执行
                Object result = methodProxy.invokeSuper(o, args);

                // 通过方法名解析出字段，获取fieldMeta
                String methodName = method.getName();
                if (!methodName.startsWith("get")) {
                    // 只能解析get开头的方法
                    return result;
                }


                SQLSingleValueElementDefinition SQLSingleValueElementDefinition = THREAD_LOCAL.get();
                if (SQLSingleValueElementDefinition == null) {
                    char[] methodNameArr = methodName.toCharArray();
                    char[] fieldNameArr = new char[methodNameArr.length - 3];
                    methodNameArr[3] = (char) (methodNameArr[3] + 32);

                    System.arraycopy(methodNameArr, 3, fieldNameArr, 0, methodNameArr.length - 3);
                    String fieldName = new String(fieldNameArr);

                    SQLFieldMeta fieldMeta = modelMeta.getFieldMeta(fieldName);

                    THREAD_LOCAL.set(new FieldDefinition(modelAlias, modelMeta, fieldMeta));
                }
                return result;
            }
        });
        Object proxyModel = enhancer.create();
        // 生成from代理节点
        ModelProxyNode modelProxyNode = new ModelProxyNode(this, proxyModel, modelAlias, modelMeta);

        this.fromNodeList.add(modelProxyNode);
        this.proxy_fromNode_map.put(modelProxyNode.getProxyModel(), modelProxyNode);
        return modelProxyNode;
    }

    private Object proxyDONewInstance(Class<?> clazz) {
        try {
            // TODO 实例化这里注意下
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void proxyDOFieldSetValue(Field field, Object o, Object value) {
        try {
            field.setAccessible(true);
            field.set(o, value);
        } catch (IllegalAccessException e) {
            throw UnbelievableException.unbelievable();
        }
    }

    @Override
    public Object getProxyModel() {
        return this.proxyModel;
    }

    @Override
    public Class<?> getFromModelClass() {
        return fromModelClass;
    }

    public Class<?> getSelectModelClass() {
        return selectModelClass;
    }

    @Override
    public QueryProxyNode getParentNode() {
        return this.parentNode;
    }

    public List<FromNode> getFromNodeList() {
        return fromNodeList;
    }

    public List<SelectNode> getSelectNodeList() {
        return selectNodeList;
    }

    public List<QueryProxyNode> getConditionSubQueryList() {
        return conditionSubQueryList;
    }

    /**
     * Description 通过代理模型查询对应的fromNode
     *
     * @author zhaolaiyuan
     * Date 2022/7/10 10:35
     **/
    public FromNode getFromNode(Object proxyModel) {
        return this.proxy_fromNode_map.get(proxyModel);
    }

    public void addSelectNode(FieldProxyNode subQueryNode) {
        this.selectNodeList.add(subQueryNode);
    }

    public void addSelectNode(QueryProxyNode subQueryNode, SqlQueryDefinition subQueryDefinition) {
        this.selectNodeList.add(subQueryNode);
        this.acceptSubQueryProxyModel(subQueryNode, subQueryDefinition, null, null);
    }


    public void addConditionSubQueryNode(QueryProxyNode subQueryNode, SqlQueryDefinition subQueryDefinition) {
        this.conditionSubQueryList.add(subQueryNode);
        this.acceptSubQueryProxyModel(subQueryNode, subQueryDefinition, null, null);
    }

    public Field getField(FromNode subFromNode) {
        return fromNode_field_map.get(subFromNode);
    }

    public SQLSingleValueElementDefinition parseField(FieldMapping<Object, Object> fieldMapping) {
        fieldMapping.getMapping(proxyModel);
        return THREAD_LOCAL.get();
    }

    public SQLSingleValueElementDefinition parseField(JoinFieldMapping<Object> fieldMapping) {
        fieldMapping.getMapping();
        return THREAD_LOCAL.get();
    }

    @Override
    public SQLAliasManager.AliasFlag getAliasFlag() {
        return aliasFlag;
    }

    @Override
    public List<MapDefinition> getMapDefinitionList() {
        return mapDefinitionList;
    }

    @Override
    public QueryFromMeta getQueryFromMeta() {
        return this.sqlQueryDefinition;
    }

    public void addMappingDefinitionList(List<MapDefinition> mapDefinitionList) {
        this.mapDefinitionList.addAll(mapDefinitionList);
    }

    public SqlQueryDefinition getSqlQueryDefinition() {
        return sqlQueryDefinition;
    }

    public void setSqlQueryDefinition(SqlQueryDefinition sqlQueryDefinition) {
        this.sqlQueryDefinition = sqlQueryDefinition;
    }
}
