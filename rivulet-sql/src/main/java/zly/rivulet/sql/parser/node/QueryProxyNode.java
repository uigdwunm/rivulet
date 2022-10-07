package zly.rivulet.sql.parser.node;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definer.annotations.SQLModelJoin;
import zly.rivulet.sql.definer.annotations.SQLSubQuery;
import zly.rivulet.sql.definer.annotations.SqlQueryAlias;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.definition.query.main.FromDefinition;
import zly.rivulet.sql.definition.query.main.SelectDefinition;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.SqlParser;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryProxyNode implements FromNode, SelectNode {
    ThreadLocal<SQLSingleValueElementDefinition> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 当前query的所有select
     **/
    private final List<SelectNode> selectNodeList = new ArrayList<>();

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
    private final List<QueryProxyNode> conditionSubQueryList = new ArrayList<>();

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

    private final SqlParser sqlParser;

    private final SqlDefiner sqlDefiner;

    /**
     * 当前节点的别名flag
     **/
    private final SQLAliasManager.AliasFlag aliasFlag = SQLAliasManager.createModelAlias();

    /**
     * 整个definition解析完还会把结果塞到这里
     **/
    private SqlQueryDefinition sqlQueryDefinition;

    private final List<MapDefinition> mapDefinitionList = new ArrayList<>();

    /**
     * 结果对象字段名和MappingDefinition的映射，vo结果并且子查询时才有
     **/
    private final Map<String, MapDefinition> mappingDefinitionMap = new HashMap<>();

    public QueryProxyNode(SqlParserPortableToolbox toolbox, Class<?> mainFrom) {
        this.sqlParser = toolbox.getSqlPreParser();
        this.sqlDefiner = (SqlDefiner) sqlParser.getDefiner();
        this.fromModelClass = mainFrom;
        if (QueryComplexModel.class.isAssignableFrom(mainFrom)) {
            this.registerComplexProxyModel(mainFrom);
        } else {
            SqlDefiner sqlDefiner = (SqlDefiner) toolbox.getSqlPreParser().getDefiner();
            SQLModelMeta modelMeta = sqlDefiner.createOrGetModelMeta(mainFrom);
            SQLAliasManager.AliasFlag modelAlias = SQLAliasManager.createModelAlias(modelMeta.getTableName(), modelMeta);
            // 生成from代理节点
            ModelProxyNode modelProxyNode = registerFromModel(modelAlias, modelMeta);
            this.proxyModel = modelProxyNode.getProxyModel();
        }
    }

    public void registerComplexProxyModel(Class<?> clazz) {
        Object o = this.proxyDONewInstance(clazz);
        // 每个字段注入代理对象
        for (Field field : clazz.getDeclaredFields()) {
            // 表示连接了一个子查询的注解
            SQLSubQuery sqlSubJoin = field.getAnnotation(SQLSubQuery.class);

            // 别名
            SqlQueryAlias sqlQueryAlias = field.getAnnotation(SqlQueryAlias.class);
            FromNode fromNode;

            if (sqlSubJoin == null) {
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
                QueryProxyNode currNode = toolbox.getCurrNode();
                Blueprint blueprint = sqlPreParser.parseByKey(sqlSubJoin.value(), toolbox);
                SqlQueryDefinition subQueryDefinition = (SqlQueryDefinition) blueprint;
                // 上面的解析过程会把curr替换成子查询对应的node
                QueryProxyNode subNode = toolbox.getCurrNode();
                subNode.sqlQueryDefinition = subQueryDefinition;

                this.acceptSubQueryProxyModel(subNode, subQueryDefinition, sqlQueryAlias, field.getName());
                // 这里替换回来
                toolbox.setCurrNode(currNode);

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
            SetMapping<Object, ?> selectField = (SetMapping<Object, ?>) mapDefinition.getSelectField();
            String fieldName = parseFieldName(selectField);
            mappingDefinitionMap.put(fieldName, mapDefinition);
        }

    }

    private String parseFieldName(SetMapping<Object, ?> selectField) {
        String methodName = selectField.parseExecuteMethodName();
        char[] methodNameArr = methodName.toCharArray();
        char[] fieldNameArr = new char[methodNameArr.length - 3];
        methodNameArr[3] = (char) (methodNameArr[3] + 32);

        System.arraycopy(methodNameArr, 3, fieldNameArr, 0, methodNameArr.length - 3);
        return new String(fieldNameArr);
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
                if (!StringUtil.checkGetterMethodName(methodName)) {
                    // 只能解析get开头的方法
                    return result;
                }


                SQLSingleValueElementDefinition SQLSingleValueElementDefinition = THREAD_LOCAL.get();
                if (SQLSingleValueElementDefinition == null) {
                    String fieldName = StringUtil.parseGetterMethodNameToFieldName(methodName);
                    THREAD_LOCAL.set(new FieldDefinition(modelMeta, fieldName));
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
        SQLSingleValueElementDefinition sqlSingleValueElementDefinition = THREAD_LOCAL.get();
        THREAD_LOCAL.remove();
        return sqlSingleValueElementDefinition;
    }

    public SQLSingleValueElementDefinition parseField(JoinFieldMapping<Object> fieldMapping) {
        fieldMapping.getMapping();
        SQLSingleValueElementDefinition sqlSingleValueElementDefinition = THREAD_LOCAL.get();
        THREAD_LOCAL.remove();
        return sqlSingleValueElementDefinition;
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

    public QueryProxyNode createSubQueryProxyNode(SqlQueryDefinition sqlQueryDefinition, SqlParserPortableToolbox toolbox, Class<?> mainFrom) {
        QueryProxyNode queryProxyNode = new QueryProxyNode(toolbox, mainFrom);
        // TODO
    }
}
