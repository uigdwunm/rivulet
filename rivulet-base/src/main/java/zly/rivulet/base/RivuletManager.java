package zly.rivulet.base;

import zly.rivulet.base.analyzer.Analyzer;
import zly.rivulet.base.assembly_line.AssemblyLine;
import zly.rivulet.base.assembly_line.Fish;
import zly.rivulet.base.assembly_line.param_manager.ForTestParamManager;
import zly.rivulet.base.assembly_line.param_manager.ParamManager;
import zly.rivulet.base.assembly_line.param_manager.SimpleParamManager;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.executor.Executor;
import zly.rivulet.base.parser.PreParser;
import zly.rivulet.base.parser.param.ParamDefinitionManager;
import zly.rivulet.base.warehouse.WarehouseManager;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RivuletManager {

    private final PreParser preParser;

    private final AssemblyLine assemblyLine;

    private final Analyzer analyzer;

    private final Executor executor;

    protected final RivuletProperties configProperties;

    protected final ConvertorManager convertorManager;

    private final WarehouseManager warehouseManager;

    private final Map<Method, Blueprint> mapperMethod_FinalDefinition_Map = new ConcurrentHashMap<>();

    protected RivuletManager(AssemblyLine assemblyLine, Analyzer analyzer, Executor executor, RivuletProperties configProperties, ConvertorManager convertorManager, WarehouseManager warehouseManager, PreParser preParser) {
        this.assemblyLine = assemblyLine;
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
            Blueprint blueprint = preParser.parse(key);
            ParamDefinitionManager paramDefinitionManager = blueprint.getParamDefinitionManager();
            paramDefinitionManager.registerMethod(method);

            mapperMethod_FinalDefinition_Map.put(method, blueprint);
        }
    }

    public Object exec(Method proxyMethod, Object[] args) {
        Blueprint blueprint = mapperMethod_FinalDefinition_Map.get(proxyMethod);
        if (blueprint == null) {
            // 没有预先定义方法
            throw ParseException.undefinedMethod();
        }
        ParamDefinitionManager paramDefinitionManager = blueprint.getParamDefinitionManager();
        ParamManager paramManager = paramDefinitionManager.getParamManager(proxyMethod, args);

        Fish fish = assemblyLine.generate(blueprint, paramManager);
        fish = analyzer.runTimeAnalyze(fish);

        return executor.queryOne(fish, blueprint.getAssigner());
    }

    public Fish exec(WholeDesc wholeDesc, Map<String, Object> params) {
        Blueprint blueprint = preParser.parse(wholeDesc);

        SimpleParamManager simpleParamManager = new SimpleParamManager(params);
        return assemblyLine.generate(blueprint, simpleParamManager);
    }

    public Fish test(WholeDesc wholeDesc) {
        Blueprint blueprint = preParser.parse(wholeDesc);

        return assemblyLine.generate(blueprint, new ForTestParamManager());
    }
}
