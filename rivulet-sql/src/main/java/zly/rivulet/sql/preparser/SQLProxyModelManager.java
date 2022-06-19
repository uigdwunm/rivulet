package zly.rivulet.sql.preparser;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definer.annotations.SQLModelJoin;
import zly.rivulet.sql.definer.annotations.SQLSubJoin;
import zly.rivulet.sql.definer.annotations.SqlQueryAlias;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Description 代理对象管理器
 * 每次预解析一个语句时生成，每次解析时都是新的
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
    private final Map<Object, QueryFromMeta> proxy_queryMeta_map = new HashMap<>();

    private ProxyNode curr;

    public SQLProxyModelManager(SqlPreParseHelper sqlPreParseHelper) {
        this.sqlPreParseHelper = sqlPreParseHelper;
        this.curr = ProxyNode.craeteRoot();
    }

    public Object createComplexProxyModel(Class<?> clazz, Method proxyMethod) {
        SqlPreParser sqlPreParser = sqlPreParseHelper.getSqlPreParser();
        SqlDefiner sqlDefiner = sqlPreParser.getSqlDefiner();
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
                Object fieldProxy = this.registerSingleModel(field.getType());
                this.proxyDOFieldSetValue(field, o, fieldProxy);

                // 找到对应的queryMeta存起来，等会会来取
                proxy_queryMeta_map.put(o, sqlDefiner.createOrGetModelMeta(clazz));
            } else if (sqlSubJoin != null) {
                // TODO 检查vo对象是否是子查询的vo

                // 注入代理对象
                Object fieldProxy = this.registerSingleModel(field.getType());
                this.proxyDOFieldSetValue(field, o, fieldProxy);

                // 递归解析子查询
                //TODO 这里仍然拿不到 子查询的对象


                FinalDefinition finalDefinition = sqlPreParser.parse(sqlSubJoin.value(), sqlPreParseHelper);
                if (finalDefinition instanceof SqlQueryDefinition) {
                    SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) finalDefinition;
                    SQLAliasManager subAliasManager = sqlQueryDefinition.getAliasManager();
                    aliasManager.merge(subAliasManager);
                    proxy_queryMeta_map.put(o, sqlQueryDefinition);
                } else if (finalDefinition instanceof HalfFinalDefinition) {
                    // 循环依赖了子查询
                    throw SQLDescDefineException.subQueryLoopNesting();
                } else {
                    // 非查询类型的
                    throw SQLDescDefineException.subQueryNotQuery();
                }
            }

            // 处理别名
            SqlQueryAlias sqlQueryAlias = field.getAnnotation(SqlQueryAlias.class);
            if (sqlQueryAlias != null && StringUtil.isNotBlank(sqlQueryAlias.value())) {
                proxy_aliasFlag_map.put(fieldProxy, aliasManager.createFlag(sqlQueryAlias.value()));
            } else {
                proxy_aliasFlag_map.put(fieldProxy, aliasManager.createFlag(field.getName()));
            }

        }

        return o;
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

    public Object registerSingleModel(Class<?> clazz) {
        SqlDefiner sqlDefiner = sqlPreParseHelper.getSqlPreParser().getSqlDefiner();
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

                    SQLModelMeta modelMeta = sqlDefiner.createOrGetModelMeta(o.getClass());
                    SQLFieldMeta fieldMeta = modelMeta.getFieldMeta(fieldName);
                    SQLAliasManager.AliasFlag aliasFlag = proxy_aliasFlag_map.get(o);

                    THREAD_LOCAL.set(new FieldDefinition(aliasFlag, modelMeta, fieldMeta));
                }
                return result;
            }
        });
        Object proxyModel = enhancer.create();
        curr.createSub(proxyModel);
        return proxyModel;
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
            return this.createComplexProxyModel(mainFrom, sqlPreParseHelper.getMethod());
        } else {
            return registerSingleModel(mainFrom);
        }
    }

    public ProxyNode getRootProxyNode() {
        return curr;
    }
}
