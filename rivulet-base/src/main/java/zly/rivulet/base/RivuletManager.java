package zly.rivulet.base;

import zly.rivulet.base.analyzer.Analyzer;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.describer.Desc;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.executor.Executor;
import zly.rivulet.base.preparser.PreParser;
import zly.rivulet.base.preparser.Rock;
import zly.rivulet.base.runparser.Fish;
import zly.rivulet.base.runparser.RuntimeParser;
import zly.rivulet.base.runparser.param_manager.ParamManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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

    private final Map<String, Definition> definitionMap = new ConcurrentHashMap<>();

    private final Map<Method, Rock> methodRockMap = new ConcurrentHashMap<>();

    protected RivuletManager(PreParser preParser, RuntimeParser runtimeParser, Analyzer analyzer, Executor executor, RivuletProperties configProperties, ConvertorManager convertorManager) {
        this.preParser = preParser;
        this.runtimeParser = runtimeParser;
        this.analyzer = analyzer;
        this.executor = executor;
        this.configProperties = configProperties;
        this.convertorManager = convertorManager;
    }

    public void initParser(List<Desc> allDescList, List<Method> allProxyMethod) {
        for (Desc desc : allDescList) {
            String key = "";
            if (definitionMap.get(key) == null) {
                Definition definition = preParser.parse(desc);
                definitionMap.put(key, definition);
            }
        }
        for (Method proxyMethod : allProxyMethod) {
            Annotation annotation = ;
            if (annotation == null) {
                throw
            }
            String key = "";
            Definition definition = definitionMap.get(key);
            if (definition == null) {
                throw
            }
            Rock rock = preParser.bind(definition, proxyMethod);
            rock = analyzer.preAnalyze(rock);
            methodRockMap.put(proxyMethod, rock);
        }
    }


    public Object exec(Method method, ParamManager paramManager) {
        Rock rock = methodRockMap.get(method);
        if (rock == null) {
            // 没有预先定义方法
            throw ParseException.undefinedMethod();
        }

        Fish fish = runtimeParser.parse(rock.getDefinition(), paramManager);
        fish = analyzer.runTimeAnalyze(fish);

        return executor.execute(fish, rock.getMapDefinition());


    }
}
