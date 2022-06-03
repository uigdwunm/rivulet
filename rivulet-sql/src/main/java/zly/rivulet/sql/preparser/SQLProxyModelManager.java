package zly.rivulet.sql.preparser;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.exception.ModelDefineException;
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
    private final Object mainProxyModel;

    private final SqlDefiner definer;

    private final SqlPreParser sqlPreParser;

    private final SQLAliasManager aliasManager;

    private final Map<Object, SQLAliasManager.AliasFlag> proxy_aliasFlag_map = new HashMap<>();

    private final Map<Object, QueryFromMeta> proxy_queryMeta_map = new HashMap<>();

    public SQLProxyModelManager(Class<?> clazz, SqlDefiner definer, SqlPreParser sqlPreParser, SQLAliasManager aliasManager, Method proxyMethod) {
        if (QueryComplexModel.class.isAssignableFrom(clazz)) {
            this.mainProxyModel = this.createComplexProxyModel(clazz, proxyMethod);
        } else {
            this.mainProxyModel = createProxyModel(clazz);
        }
        this.definer = definer;
        this.sqlPreParser = sqlPreParser;
        this.aliasManager = aliasManager;
    }

    public Object createComplexProxyModel(Class<?> clazz, Method proxyMethod) {
        Object o = this.proxyDONewInstance(clazz);
        // 每个字段注入代理对象
        for (Field field : clazz.getDeclaredFields()) {
            SQLModelJoin sqlModelJoin = field.getAnnotation(SQLModelJoin.class);
            SQLSubJoin sqlSubJoin = field.getAnnotation(SQLSubJoin.class);
            if ((sqlModelJoin != null && sqlSubJoin != null) || (sqlModelJoin == null && sqlSubJoin == null)) {
                // 两个注解都有，或两个注解都没有，报错
                throw SQLDescDefineException.unknowQueryType();
            }
            // 只要存在一个，则注入代理对象
            Object fieldProxy = this.createProxyModel(field.getType());
            this.proxyDOFieldSetValue(field, o, fieldProxy);

            if (sqlModelJoin != null) {
                // TODO 检查DO对象是否是表，

                // 找到对应的queryMeta存起来，等会会来取
                proxy_queryMeta_map.put(o, definer.createOrGetModelMeta(clazz));
            } else if (sqlSubJoin != null) {
                // TODO 检查vo对象是否是子查询的vo

                // 找到对应的queryMeta存起来，等会会来取
                FinalDefinition finalDefinition = sqlPreParser.parse(sqlSubJoin.value(), proxyMethod);
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

    public Object createProxyModel(Class<?> clazz) {
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

                    SQLModelMeta modelMeta = definer.createOrGetModelMeta(o.getClass());
                    SQLFieldMeta fieldMeta = modelMeta.getFieldMeta(fieldName);
                    SQLAliasManager.AliasFlag aliasFlag = proxy_aliasFlag_map.get(o);

                    THREAD_LOCAL.set(new FieldDefinition(aliasFlag, modelMeta, fieldMeta));
                }
                return result;
            }
        });
        return enhancer.create();
    }

    /**
     * Description 进入了一层子查询
     *
     * @author zhaolaiyuan
     * Date 2022/4/5 15:24
     **/
    public void startSub() {
        aliasManager.startSub();
    }

    public void endSub() {
        aliasManager.endSub();
    }

    public Object getMainProxyModel() {
        return mainProxyModel;
    }

    public QueryFromMeta getQueryMeta(Object proxy) {
        return proxy_queryMeta_map.get(proxy);
    }

    public FieldDefinition parseField(FieldMapping fieldMapping) {
        fieldMapping.getMapping(mainProxyModel);
        return THREAD_LOCAL.get();
    }
}
