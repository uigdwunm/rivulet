package zly.rivulet.base;

import zly.rivulet.base.analyzer.Analyzer;
import zly.rivulet.base.adapter.BeanManager;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.annotations.RivuletQueryDesc;
import zly.rivulet.base.definer.annotations.RivuletQueryMapper;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.executor.Executor;
import zly.rivulet.base.preparser.PreParser;
import zly.rivulet.base.preparser.param.ParamDefinitionManager;
import zly.rivulet.base.runparser.Fish;
import zly.rivulet.base.runparser.RuntimeParser;
import zly.rivulet.base.runparser.param_manager.ParamManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RivuletManager {

    private final RuntimeParser runtimeParser;

    private final Analyzer analyzer;

    private final Executor executor;

    protected final RivuletProperties configProperties;

    protected final ConvertorManager convertorManager;

    private final BeanManager beanManager;

    private final Map<Method, FinalDefinition> methodFinalDefinitionMap = new ConcurrentHashMap<>();

    protected RivuletManager(RuntimeParser runtimeParser, Analyzer analyzer, Executor executor, RivuletProperties configProperties, ConvertorManager convertorManager, BeanManager beanManager) {
        this.runtimeParser = runtimeParser;
        this.analyzer = analyzer;
        this.executor = executor;
        this.configProperties = configProperties;
        this.convertorManager = convertorManager;
        this.beanManager = beanManager;
    }

    /**
     * Description 初始化方法，预创建出所有用到的definition，并按key存储好
     * 只会在启动时执行一次
     *
     * @author zhaolaiyuan
     * Date 2022/3/20 10:15
     **/
    public void preParse(List<Method> allWholeDescMethodList, List<Method> allProxyMethod) {
        Map<String, WholeDesc> wholeDescMap = this.getWholeDescMap(allWholeDescMethodList);

        PreParser preParser = this.createPreParser(wholeDescMap);

        for (Method proxyMethod : allProxyMethod) {
            RivuletQueryMapper annotation = proxyMethod.getAnnotation(RivuletQueryMapper.class);
            if (annotation == null) {
                continue;
            }
            String key = annotation.value();
            FinalDefinition finalDefinition = preParser.parse(key, proxyMethod);

            // TODO
//            finalDefinition = analyzer.preAnalyze(finalDefinition);

            methodFinalDefinitionMap.put(proxyMethod, finalDefinition);
        }
    }

    private Map<String, WholeDesc> getWholeDescMap(List<Method> allWholeDescMethodList) {
        Map<String, WholeDesc> wholeDescMap = new HashMap<>();
        try {
            for (Method method : allWholeDescMethodList) {
                RivuletQueryDesc rivuletQueryDesc = method.getAnnotation(RivuletQueryDesc.class);
                if (rivuletQueryDesc == null) {
                    continue;
                }
                String key = rivuletQueryDesc.value();
                Class<?> declaringClass = method.getDeclaringClass();
                Object o = beanManager.getBean(declaringClass);
                wholeDescMap.put(key, (WholeDesc) method.invoke(o));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO
            throw new RuntimeException(e);
        }
        return wholeDescMap;
    }


    public Object exec(Method method, Object[] args) {
        FinalDefinition finalDefinition = methodFinalDefinitionMap.get(method);
        if (finalDefinition == null) {
            // 没有预先定义方法
            throw ParseException.undefinedMethod();
        }
        ParamDefinitionManager paramDefinitionManager = finalDefinition.getParamDefinitionManager();
        ParamManager paramManager = paramDefinitionManager.getParamManager(args);

        Fish fish = runtimeParser.parse(finalDefinition, paramManager);
        fish = analyzer.runTimeAnalyze(fish);

        return executor.execute(fish, finalDefinition.getAssigner());
    }

    protected abstract PreParser createPreParser(Map<String, WholeDesc> wholeDescMap);

}
