package zly.rivulet.base;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.Definer;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.definition.param.ParamManagerFactory;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.executor.Executor;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.for_proxy_method.ForTestParamManager;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.SimpleParamManager;
import zly.rivulet.base.parser.Parser;
import zly.rivulet.base.pipeline.RunningPipeline;
import zly.rivulet.base.warehouse.WarehouseManager;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RivuletManager {

    private final Parser parser;

    private final Definer definer;

    private final Generator generator;

    private final RunningPipeline runningPipeline;

    protected final RivuletProperties configProperties;

    protected final ConvertorManager convertorManager;

    private final WarehouseManager warehouseManager;

    private final ParamManagerFactory paramManagerFactory;

    private final Map<Method, Blueprint> mapperMethod_FinalDefinition_Map = new ConcurrentHashMap<>();

    protected RivuletManager(Parser parser, Generator generator, Executor executor, RivuletProperties configProperties, ConvertorManager convertorManager, WarehouseManager warehouseManager) {
        this.parser = parser;
        this.definer = parser.getDefiner();
        this.generator = generator;
        this.runningPipeline = new RunningPipeline(generator, executor);
        this.configProperties = configProperties;
        this.convertorManager = convertorManager;
        this.warehouseManager = warehouseManager;
        this.paramManagerFactory = new ParamManagerFactory();

        // 解析
        this.preParseAll();

        // 预热
        this.warmUpAll();
    }

    /**
     * Description 初始化方法，预创建出所有用到的definition，并按key存储好
     * 只会在启动时执行一次
     *
     * @author zhaolaiyuan
     * Date 2022/3/20 10:15
     **/

    public void preParseAll() {
        Map<String, Method> allMapperMethod = warehouseManager.getAllMapperMethod();
        for (Map.Entry<String, Method> entry : allMapperMethod.entrySet()) {
            String key = entry.getKey();
            Method method = entry.getValue();

            // 解析definition
            Blueprint blueprint = parser.parseByKey(key);
            // 参数绑定设计图
            paramManagerFactory.registerProxyMethod(blueprint, method);
            // 方法绑定设计图
            mapperMethod_FinalDefinition_Map.put(method, blueprint);
        }
    }

    /**
     * Description 为所有设计图预热
     * 只会在启动时执行一次
     *
     * @author zhaolaiyuan
     * Date 2022/8/9 8:32
     **/
    public void warmUpAll() {
        for (Blueprint blueprint : mapperMethod_FinalDefinition_Map.values()) {
            runningPipeline.warmUp(blueprint);
        }
    }

    /**
     * Description 用于代理方法中调用的执行
     *
     * @author zhaolaiyuan
     * Date 2022/9/6 8:46
     **/
    public Object exec(Method proxyMethod, Object[] args) {
        Blueprint blueprint = mapperMethod_FinalDefinition_Map.get(proxyMethod);
        if (blueprint == null) {
            // 没有预先定义方法
            throw ParseException.undefinedMethod();
        }
        ParamManager paramManager = paramManagerFactory.getByProxyMethod(blueprint, proxyMethod, args);

        return runningPipeline.go(blueprint, paramManager, proxyMethod.getReturnType());
    }

    /**
     * Description 用于手动传desc和参数param，执行
     *
     * @author zhaolaiyuan
     * Date 2022/9/6 8:46
     **/
    public <T> T exec(WholeDesc wholeDesc, Map<String, Object> params, Class<T> returnType) {
        // 解析方法
        Blueprint blueprint = parser.parseByDesc(wholeDesc);

        SimpleParamManager simpleParamManager = new SimpleParamManager(params);
        return (T) runningPipeline.go(blueprint, simpleParamManager, returnType);
    }

    public <T> T insertOne(T obj) {
        Class<?> clazz = obj.getClass();
        ModelMeta modelMeta = definer.createOrGetModelMeta(clazz);
        Blueprint blueprint = parser.parseInsertByMeta(modelMeta);

        ParamManager paramManager = paramManagerFactory.getByModelMeta(modelMeta, new Object[]{obj});

        return (T) runningPipeline.go(blueprint, paramManager, clazz);
    }

    public <T> List<T> insertAll(Collection<T> batchModel) {
//        Class<?> clazz = obj.getClass();
//        ModelMeta modelMeta = definer.createOrGetModelMeta(clazz);
//        Blueprint blueprint = parser.parseInsertByMeta(modelMeta);
//
//        ParamManager paramManager = paramManagerFactory.getByModelMeta(modelMeta, new Object[]{obj});
//
//        return runningPipeline.go(blueprint, paramManager, clazz);
    }

    public <T> T updateOne(T obj) {

    }

    public <T> List<T> updateAll(Collection<T> obj) {

    }

    public <T, I> T queryById(I id, Class<T> modelClass) {

    }

    public <T, I> T queryByIds(Collection<I> ids, Class<T> modelClass) {

    }

    public <T> T queryAll(Class<T> modelClass) {

    }

    public <T, I> T deleteById(I id, Class<T> modelClass) {

    }

    public <T, I> T deleteByIds(Collection<I> ids, Class<T> modelClass) {

    }

    public <T> T deleteAll(Class<T> modelClass) {

    }

    public Fish testParse(WholeDesc wholeDesc) {
        Blueprint blueprint = parser.parseByDesc(wholeDesc);
        return generator.generate(blueprint, new ForTestParamManager());
    }
}
