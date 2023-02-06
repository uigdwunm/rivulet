package zly.rivulet.base;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.Definer;
import zly.rivulet.base.definer.annotations.RivuletDesc;
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
import zly.rivulet.base.utils.LoadUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RivuletManager {

    protected final Parser parser;

    protected final Definer definer;

    protected final Generator generator;

    protected final RunningPipeline runningPipeline;

    protected final RivuletProperties configProperties;

    protected final ConvertorManager convertorManager;

    protected final ParamManagerFactory paramManagerFactory;

    protected final CollectionInstanceCreator collectionInstanceCreator = new CollectionInstanceCreator();

    /**
     * 所有key与原始desc的映射
     **/
    private final Map<String, WholeDesc> rivuletKey_wholeDesc_map = new HashMap<>();

    /**
     * 所有key与设计图的映射
     **/
    protected final Map<String, Blueprint> rivuletKey_blueprint_map = new ConcurrentHashMap<>();

    protected RivuletManager(
        Parser parser,
        Generator generator,
        RivuletProperties configProperties,
        ConvertorManager convertorManager
    ) {
        this.parser = parser;
        this.definer = parser.getDefiner();
        this.generator = generator;
        this.runningPipeline = new RunningPipeline(generator);
        this.configProperties = configProperties;
        this.convertorManager = convertorManager;
        this.paramManagerFactory = new ParamManagerFactory();
    }

    /**
     * Description 获取一个运行时的执行器，可以理解为一个session
     *
     * @author zhaolaiyuan
     * Date 2022/12/25 10:37
     **/
    public abstract Rivulet getRivulet();

    public void putInStorageByBasePackage(String ... basePackages) {
        for (String basePackage : basePackages) {
            List<Class<?>> classes = LoadUtil.scan(basePackage);
            for (Class<?> clazz : classes) {
                this.putInStorage(clazz);
            }
        }
    }

    /**
     * Description 入库
     *
     * @author zhaolaiyuan
     * Date 2022/7/23 11:49
     **/
    public void putInStorage(Class<?> descClazz) {
        try {
            Object o = null;
            for (Method method : descClazz.getMethods()) {
                RivuletDesc rivuletDesc  = method.getAnnotation(RivuletDesc.class);
                if (rivuletDesc != null) {
                    // 是配置类
                    String key = rivuletDesc.value();
                    if (o == null) {
                        o = descClazz.newInstance();
                    }
                    WholeDesc wholeDesc = (WholeDesc) method.invoke(o);
                    wholeDesc.setAnnotation(rivuletDesc);
                    rivuletKey_wholeDesc_map.put(key, wholeDesc);

                    Blueprint blueprint = parser.parse(wholeDesc);
                    this.rivuletKey_blueprint_map.put(key, blueprint);
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void methodBinding(Method proxyMethod, String descKey) {
        // 获取上面解析好的definition
        Blueprint blueprint = rivuletKey_blueprint_map.get(descKey);
        if (blueprint == null) {
            // 没有预先定义方法
            throw ParseException.noBindingDesc(proxyMethod);
        }
        // 参数绑定设计图
        paramManagerFactory.registerProxyMethod(blueprint, proxyMethod);
    }

    /**
     * Description 为所有设计图预热
     * 只需要在启动时执行一次
     *
     * @author zhaolaiyuan
     * Date 2022/8/9 8:32
     **/
    public void warmUpAll() {
        for (Blueprint blueprint : rivuletKey_blueprint_map.values()) {
            generator.warmUp(blueprint);
        }
    }

    public Fish testParse(WholeDesc wholeDesc) {
        Blueprint blueprint = parser.parse(wholeDesc);
        return generator.generate(blueprint, new ForTestParamManager());
    }

    public Fish testParse(String rivuletKey) {
        // 解析definition
        WholeDesc wholeDesc = rivuletKey_wholeDesc_map.get(rivuletKey);
        Blueprint blueprint = parser.parse(wholeDesc);
        return generator.generate(blueprint, new ForTestParamManager());
    }

    public RunningPipeline getRunningPipeline() {
        return runningPipeline;
    }

    public Parser getParser() {
        return parser;
    }

    public ConvertorManager getConvertorManager() {
        return convertorManager;
    }

    public Blueprint getBlueprintByDescKey(String descKey) {
        return rivuletKey_blueprint_map.get(descKey);
    }

    public WholeDesc getWholeDescByDescKey(String descKey) {
        return rivuletKey_wholeDesc_map.get(descKey);
    }
}
