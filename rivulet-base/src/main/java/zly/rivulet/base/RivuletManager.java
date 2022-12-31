package zly.rivulet.base;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.Definer;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.definition.param.ParamManagerFactory;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.exception.ParseException;
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

public abstract class RivuletManager {

    protected final Parser parser;

    protected final Definer definer;

    protected final Generator generator;

    protected final RunningPipeline runningPipeline;

    protected final RivuletProperties configProperties;

    protected final ConvertorManager convertorManager;

    protected final WarehouseManager warehouseManager;

    protected final ParamManagerFactory paramManagerFactory;

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
     * Description 获取一个运行时的执行器，可以理解为一个session
     *
     * @author zhaolaiyuan
     * Date 2022/12/25 10:37
     **/
    public abstract Rivulet getRivulet();

    /**
     * Description 初始化方法，预创建出所有用到的definition，并按key存储好
     * 只需要在启动时执行一次
     *
     * @author zhaolaiyuan
     * Date 2022/3/20 10:15
     **/

    public void preParseAll() {
        // 解析所有配置了key的desc
        Map<String, WholeDesc> allConfiguredDesc = warehouseManager.getAllConfiguredDesc();
        for (Map.Entry<String, WholeDesc> entry : allConfiguredDesc.entrySet()) {
            String key = entry.getKey();
            WholeDesc wholeDesc = entry.getValue();

            Blueprint blueprint = parser.parse(wholeDesc);
            warehouseManager.putDescKeyBlueprint(key, blueprint);
        }

        // 将所有关联了代理方法的key与method绑定
        Map<String, Method> allMapperMethod = warehouseManager.getAllMapperMethod();
        for (Map.Entry<String, Method> entry : allMapperMethod.entrySet()) {
            String key = entry.getKey();
            Method method = entry.getValue();

            // 获取上面解析好的definition
            Blueprint blueprint = warehouseManager.getByDescKey(key);
            if (blueprint == null) {
                // 没有预先定义方法
                throw ParseException.noBindingDesc(method);
            }
            // 参数绑定设计图
            paramManagerFactory.registerProxyMethod(blueprint, method);
            // 方法绑定设计图
            warehouseManager.putProxyMethodBlueprint(method, blueprint);
        }
    }

    /**
     * Description 为所有设计图预热
     * 只需要在启动时执行一次
     *
     * @author zhaolaiyuan
     * Date 2022/8/9 8:32
     **/
    public void warmUpAll() {
        for (Blueprint blueprint : warehouseManager.getAllBlueprint()) {
            generator.warmUp(blueprint);
        }
    }

    public Fish testParse(WholeDesc wholeDesc) {
        Blueprint blueprint = parser.parse(wholeDesc);
        return generator.generate(blueprint, new ForTestParamManager());
    }

    public Fish testParse(String rivuletKey) {
        // 解析definition
        WholeDesc wholeDesc = warehouseManager.getWholeDesc(rivuletKey);
        Blueprint blueprint = parser.parse(wholeDesc);
        return generator.generate(blueprint, new ForTestParamManager());
    }

    public RunningPipeline getRunningPipeline() {
        return runningPipeline;
    }

    public Parser getParser() {
        return parser;
    }
}
