package zly.rivulet.sql.preparser;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.mapper.MapDefinition;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definer.annotations.SQLModelJoin;
import zly.rivulet.sql.definer.annotations.SQLSubJoin;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.HalfFinalDefinition;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;
import zly.rivulet.sql.preparser.helper.node.ProxyNode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Description 代理对象管理器
 * 每次预解析一个语句时生成，每次解析时都是新的
 *
 * 代理对象有三种可能
 * 1，直接对应表的DO对象
 * 2，对应复杂查询的DO对象
 * 3，对应复杂查询的vo对象，参数传的是key
 *
 * @author zhaolaiyuan
 * Date 2022/5/4 13:57
 **/
public class SQLProxyModelManager {

    private final static ThreadLocal<FieldDefinition> THREAD_LOCAL = new ThreadLocal<>();

    private final SqlPreParseHelper sqlPreParseHelper;
    /**
     * 每个代理对象，都对应自己专属的QueryFromMeta
     **/
//    private final Map<Object, ProxyNode> proxy_queryMeta_map = new HashMap<>();

    private ProxyNode curr;

    public SQLProxyModelManager(SqlPreParseHelper sqlPreParseHelper) {
        this.sqlPreParseHelper = sqlPreParseHelper;
        this.curr = ProxyNode.craeteRoot();
    }

    public Object createComplexProxyModel(Class<?> clazz) {
        SqlPreParser sqlPreParser = sqlPreParseHelper.getSqlPreParser();
        SqlDefiner sqlDefiner = sqlPreParser.getSqlDefiner();
        SQLAliasManager.complexQueryAlias()
        SQLAliasManager aliasManager = sqlPreParseHelper.getAliasManager();
        Object o = this.proxyDONewInstance(clazz);
        // 每个字段注入代理对象
        for (Field field : clazz.getDeclaredFields()) {
            SQLModelJoin sqlModelJoin = field.getAnnotation(SQLModelJoin.class);
            SQLSubJoin sqlSubJoin = field.getAnnotation(SQLSubJoin.class);
            if ((sqlModelJoin != null && sqlSubJoin != null) || (sqlModelJoin == null && sqlSubJoin == null)) {
                // 两个注解都有，或两个注解都没有，报错
                throw SQLDescDefineException.unknowQueryType();
            }

            if (sqlModelJoin != null) {
                // TODO 检查DO对象是否是表，

                // 注入代理对象
                Object fieldProxy = this.registerSingleModel(field.getType(), field.getName());
                this.proxyDOFieldSetValue(field, o, fieldProxy);

                // 找到对应的queryMeta存起来，等会会来取
                proxy_queryMeta_map.put(o, sqlDefiner.createOrGetModelMeta(clazz));
            } else if (sqlSubJoin != null) {
                // TODO 检查vo对象是否是子查询的vo

                // 保留当前的外层node节点
                ProxyNode outer = this.curr;
                FinalDefinition finalDefinition = sqlPreParser.parse(sqlSubJoin.value(), sqlPreParseHelper);
                // 上面的解析过程会把curr替换成子查询对应的node
                ProxyNode subNode = this.curr;
                Object proxyMode = this.registerProxyModel(finalDefinition);
                subNode.setProxyModel(proxyMode);
                // 这里替换回来
                this.curr = outer;
                this.curr.addSubNode(subNode);

                // 注入代理对象
                this.proxyDOFieldSetValue(field, o, proxyMode);
            }

        }

        return o;
    }

    public Object createSingleModel(Class<?> clazz, String name) {
        SqlDefiner sqlDefiner = sqlPreParseHelper.getSqlPreParser().getSqlDefiner();
        SQLModelMeta modelMeta = sqlDefiner.createOrGetModelMeta(clazz);
        SQLAliasManager.AliasFlag aliasFlag = SQLAliasManager.singleQueryAlias(name);
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
        curr.createSub(proxyModel);
        return proxyModel;
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

    public Object getMainProxyModel() {
        return curr.getProxyModel();
    }

    public QueryFromMeta getQueryMeta(Object proxy) {
        return proxy_queryMeta_map.get(proxy);
    }

    public FieldDefinition parseField(FieldMapping fieldMapping) {
        fieldMapping.getMapping(curr.getProxyModel());
        return THREAD_LOCAL.get();
    }

    public Object registerProxyModel(Class<?> mainFrom) {
        if (QueryComplexModel.class.isAssignableFrom(mainFrom)) {
            return this.createComplexProxyModel(mainFrom);
        } else {
            return registerSingleModel(mainFrom, field.getName());
        }
    }

    /**
     * Description 生成子查询的对象对象，子查询需要生成对象是，外层先要preParser解析出finalDefinition，并且也挂载好了proxyNode
     *
     * @author zhaolaiyuan
     * Date 2022/6/19 11:21
     **/
    public Object registerProxyModel(FinalDefinition finalDefinition) {
        if (finalDefinition instanceof SqlQueryDefinition) {
            SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) finalDefinition;
            Class<?> resultModelClass = sqlQueryDefinition.getMapDefinition().getResultModelClass();
            Class<?> fromMode = sqlQueryDefinition.getFromDefinition().getFromMode();
            if (resultModelClass.equals(fromMode)) {
                // 出参就是from对象
                return this.createComplexProxyModel(resultModelClass);
            } else {
                // 是vo对象
                return ;
            }
        } else if (finalDefinition instanceof HalfFinalDefinition) {
            // 循环依赖了子查询
            throw SQLDescDefineException.subQueryLoopNesting();
        } else {
            // 非查询类型的
            throw SQLDescDefineException.subQueryNotQuery();
        }
    }

    public ProxyNode getRootProxyNode() {
        return curr;
    }
}
