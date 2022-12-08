package zly.rivulet.base;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.Definer;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.definition.param.ParamManagerFactory;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.for_proxy_method.ForTestParamManager;
import zly.rivulet.base.parser.Parser;
import zly.rivulet.base.pipeline.RunningPipeline;
import zly.rivulet.base.utils.CollectionInstanceCreator;
import zly.rivulet.base.warehouse.WarehouseManager;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RivuletManager implements DefaultOperation {

    protected final Parser parser;

    protected final Definer definer;

    protected final Generator generator;

    protected final RunningPipeline runningPipeline;

    protected final RivuletProperties configProperties;

    protected final ConvertorManager convertorManager;

    protected final WarehouseManager warehouseManager;

    protected final ParamManagerFactory paramManagerFactory;

    protected final Map<Method, Blueprint> mapperMethod_FinalDefinition_Map = new ConcurrentHashMap<>();

    protected final CollectionInstanceCreator collectionInstanceCreator = new CollectionInstanceCreator();

    protected RivuletManager(
        Parser parser,
        Generator generator,
        RivuletProperties configProperties,
        ConvertorManager convertorManager,
        WarehouseManager warehouseManager
    ) {
        this.parser = parser;
        this.definer = parser.getDefiner();
        this.generator = generator;
        this.runningPipeline = new RunningPipeline(generator);
        this.configProperties = configProperties;
        this.convertorManager = convertorManager;
        this.warehouseManager = warehouseManager;
        this.paramManagerFactory = new ParamManagerFactory();
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
            generator.warmUp(blueprint);
        }
    }

    public abstract Object exec(Method proxyMethod, Object[] args);

    public Fish testParse(WholeDesc wholeDesc) {
        Blueprint blueprint = parser.parseByDesc(wholeDesc);
        return generator.generate(blueprint, new ForTestParamManager());
    }
}
