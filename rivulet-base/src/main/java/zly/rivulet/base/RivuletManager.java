package zly.rivulet.base;

import zly.rivulet.base.analyzer.Analyzer;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.executor.Executor;
import zly.rivulet.base.preparser.PreParser;
import zly.rivulet.base.runparser.Fish;
import zly.rivulet.base.runparser.RuntimeParser;
import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.base.utils.EchoUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RivuletManager {

    private final PreParser preParser;

    private final RuntimeParser runtimeParser;

    private final Analyzer analyzer;

    private final Executor executor;

    private final RivuletProperties configProperties;

    protected final ConvertorManager convertorManager;


    private final Map<String, WholeDesc> wholeDescMap = new HashMap<>();

    private final Map<Method, FinalDefinition> methodFinalDefinitionMap = new ConcurrentHashMap<>();

    protected RivuletManager(PreParser preParser, RuntimeParser runtimeParser, Analyzer analyzer, Executor executor, RivuletProperties configProperties, ConvertorManager convertorManager) {
        this.preParser = preParser;
        this.runtimeParser = runtimeParser;
        this.analyzer = analyzer;
        this.executor = executor;
        this.configProperties = configProperties;
        this.convertorManager = convertorManager;
    }

    /**
     * Description 初始化方法，预创建出所有用到的definition，并按key存储好
     * 只会在启动时执行一次
     *
     * @author zhaolaiyuan
     * Date 2022/3/20 10:15
     **/
    public void initParser(List<WholeDesc> allWholeDescList, List<Method> allProxyMethod) {
        try {
            Map<String, WholeDesc> wholeDescMap = this.wholeDescMap;
            for (WholeDesc wholeDesc : allWholeDescList) {
                String key = ;
                wholeDescMap.put(key, wholeDesc);
            }

            for (Method proxyMethod : allProxyMethod) {
                Annotation annotation = ;
                if (annotation == null) {
                    throw
                }
                String key = ;
                WholeDesc wholeDesc = wholeDescMap.get(key);
                if (wholeDesc == null) {
                    throw
                }
                FinalDefinition finalDefinition = preParser.parse(wholeDesc, proxyMethod);

                finalDefinition = analyzer.preAnalyze(finalDefinition);

                methodFinalDefinitionMap.put(proxyMethod, finalDefinition);
            }
        } finally {
            // 清理回响
            EchoUtil.clear();
        }
    }


    public Object exec(Method method, ParamManager paramManager) {
        try {
            FinalDefinition finalDefinition = methodFinalDefinitionMap.get(method);
            if (finalDefinition == null) {
                // 没有预先定义方法
                throw ParseException.undefinedMethod();
            }

            Fish fish = runtimeParser.parse(finalDefinition, paramManager);
            fish = analyzer.runTimeAnalyze(fish);

            return executor.execute(fish, finalDefinition.getMapDefinition());
        } finally {
            // 清理回响
            EchoUtil.clear();
        }
    }
}
