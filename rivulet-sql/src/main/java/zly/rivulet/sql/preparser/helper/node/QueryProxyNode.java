package zly.rivulet.sql.preparser.helper.node;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.mapper.MapDefinition;
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
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.preparser.SqlPreParser;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class QueryProxyNode implements FromNode, SelectNode {

    /**
     * 当前query的所有select
     **/
    private List<SelectNode> selectNodeList;

    /**
     * 当前query的所有from，如果是单表查询则只有一个，但是不会为空
     **/
    private final List<FromNode> fromNodeList = new ArrayList<>();

    private final Map<FromNode, Field> fromNode_field_map = new HashMap<>();

    private final Map<Object, FromNode> proxy_fromNode_map = new HashMap<>();

    /**
     * where条件中如果出现子查询也记录到这里
     **/
    private List<QueryProxyNode> whereSubQueryList;

    /**
     * 当前节点所属的父节点,如果是根节点则为null
     **/
    private QueryProxyNode parentNode;

    /**
     * 当前查询的definition，如果是子查询的时候，外层会取这个值
     **/
    private SqlQueryDefinition subQueryDefinition;

    /**
     * 当前对象的代理对象，联表查询的代理对象，或者单表查询的表对象
     *
     **/
    private Object proxyModel;

    private final Class<?> modelClass;

    private final SqlPreParseHelper sqlPreParseHelper;

    /**
     * 当前节点的别名flag,如果是最外层的查询，则为空
     **/
    private SQLAliasManager.AliasFlag aliasFlag;

    private SqlQueryDefinition sqlQueryDefinition;

    /**
     * 结果对象创建，传入参数list用于收集过程中生成的赋值器
     **/
    private Function<List<Consumer<Object>>, Object> creator;

    /**
     * 是否原始查询结果对象
     **/
    private final boolean isOriginResult;

    /**
     * 这个字段是否用到了，如果是子查询的情况，有可能多个字段只用到部分
     **/
    private boolean isUsed;

    public QueryProxyNode(SqlPreParseHelper sqlPreParseHelper, SqlQueryMetaDesc<?, ?> metaDesc) {
        Class<?> mainFrom = metaDesc.getMainFrom();
        this.modelClass = mainFrom;
        this.isOriginResult = mainFrom.equals(metaDesc.getSelectModel());
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
                subNode.subQueryDefinition = subQueryDefinition;

                SQLAliasManager.AliasFlag modelAlias;
                if (sqlQueryAlias != null) {
                    modelAlias = SQLAliasManager.createModelAlias(sqlQueryAlias);
                } else {
                    modelAlias = SQLAliasManager.createModelAlias(field.getName());
                }
                this.acceptSubQueryProxyModel(subNode, subQueryDefinition, modelAlias);
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
    private void acceptSubQueryProxyModel(QueryProxyNode subNode, SqlQueryDefinition subQueryDefinition, SQLAliasManager.AliasFlag modelAlias) {
        //
        subNode.parentNode = this;
        MapDefinition mapDefinition = subQueryDefinition.getMapDefinition();
        FromDefinition fromDefinition = subQueryDefinition.getFromDefinition();
        // 先判断 from对象和结果对象是不是同一个
        if (fromDefinition.getFromMode().equals(mapDefinition.getResultModelClass())) {
            // 结果对象和from对象是同一个,那么可以直接返回fromModel对应的代理对象
            return;
        }
        Object subQueryFromProxyModel = subNode.proxyModel;
        // TODO 结果对象是vo，需要拿到每个vo映射的subQueryModel对象字段,

        mapDefinition.get();
//        subNode.proxyModel = voProxyModel;


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


                FieldDefinition fieldDefinition = THREAD_LOCAL.get();
                if (fieldDefinition == null) {
                    char[] methodNameArr = methodName.toCharArray();
                    char[] fieldNameArr = new char[methodNameArr.length - 3];
                    methodNameArr[3] = (char) (methodNameArr[3] + 32);

                    System.arraycopy(methodNameArr, 3, fieldNameArr, 0, methodNameArr.length - 3);
                    String fieldName = new String(fieldNameArr);

                    SQLFieldMeta fieldMeta = modelMeta.getFieldMeta(fieldName);

                    THREAD_LOCAL.set(new FieldDefinition(aliasFlag, null, modelMeta, fieldMeta));
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
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
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
    public Class<?> getModelClass() {
        return modelClass;
    }

    @Override
    public QueryProxyNode getParentNode() {
        return this.parentNode;
    }

    public List<FromNode> getFromNodeList() {
        return fromNodeList;
    }

    public QueryFromMeta getFromNode(Object proxyModel) {
        return null;
    }

    public void addSelectNode(QueryProxyNode subQueryNode) {
        subQueryNode.parentNode = this;
        this.selectNodeList.add(subQueryNode);
    }

    public Field getField(FromNode subFromNode) {
        return fromNode_field_map.get(subFromNode);
    }
}
