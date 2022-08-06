package zly.rivulet.base;

import zly.rivulet.base.analyzer.Analyzer;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.executor.Executor;
import zly.rivulet.base.preparser.PreParser;
import zly.rivulet.base.preparser.param.ParamDefinitionManager;
import zly.rivulet.base.runparser.Fish;
import zly.rivulet.base.runparser.RuntimeParser;
import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.base.warehouse.WarehouseManager;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RivuletManager {

    private final PreParser preParser;

    private final RuntimeParser runtimeParser;

    private final Analyzer analyzer;

    private final Executor executor;

    protected final RivuletProperties configProperties;

    protected final ConvertorManager convertorManager;

    private final WarehouseManager warehouseManager;

    private final Map<Method, FinalDefinition> mapperMethod_FinalDefinition_Map = new ConcurrentHashMap<>();

    protected RivuletManager(RuntimeParser runtimeParser, Analyzer analyzer, Executor executor, RivuletProperties configProperties, ConvertorManager convertorManager, WarehouseManager warehouseManager, PreParser preParser) {
        this.runtimeParser = runtimeParser;
        this.analyzer = analyzer;
        this.executor = executor;
        this.configProperties = configProperties;
        this.convertorManager = convertorManager;
        this.warehouseManager = warehouseManager;
        this.preParser = preParser;

        // 预解析
        this.preParseAll();
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
            FinalDefinition finalDefinition = preParser.parse(key);
            ParamDefinitionManager paramDefinitionManager = finalDefinition.getParamDefinitionManager();
            paramDefinitionManager.registerMethod(method);

            mapperMethod_FinalDefinition_Map.put(method, finalDefinition);
        }
    }

    public Object exec(Method proxyMethod, Object[] args) {
        FinalDefinition finalDefinition = mapperMethod_FinalDefinition_Map.get(proxyMethod);
        if (finalDefinition == null) {
            // 没有预先定义方法
            throw ParseException.undefinedMethod();
        }
        ParamDefinitionManager paramDefinitionManager = finalDefinition.getParamDefinitionManager();
        ParamManager paramManager = paramDefinitionManager.getParamManager(proxyMethod, args);

        Fish fish = runtimeParser.parse(finalDefinition, paramManager);
        fish = analyzer.runTimeAnalyze(fish);

        return executor.queryOne(fish, finalDefinition.getAssigner());
    }

//    public Fish exec(WholeDesc wholeDesc, Map<String, Object> params) {
////        TestParamDefinitionManager testParamDefinitionManager = new TestParamDefinitionManager();
////        FinalDefinition finalDefinition = preParser.parse(wholeDesc, testParamDefinitionManager);
////
////        ParamManager paramManager = testParamDefinitionManager.getParamManager(null);
//
//        return runtimeParser.parse(finalDefinition, paramManager);
//    }

}
