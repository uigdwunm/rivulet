package zly.rivulet.base.convertor;

import zly.rivulet.base.exception.ModelDefineException;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.TwofoldConcurrentHashMap;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class ConvertorManager {

    private static final ResultConvertor<Object, Object> selfConvertor = new ResultConvertor<Object, Object>() {
        @Override
        public Object convert(Object originData) {
            return originData;
        }
    };

    /**
     * Description 一般用于查询结果转换成java类型时,就是 jdbc的type转换成java模型的type
     *
     * @author zhaolaiyuan
     * Date 2022/12/4 12:40
     **/
    private final TwofoldConcurrentHashMap<Class<?>, Class<?> , ResultConvertor<?, ?>> resultConvertorMap;

    private final ConcurrentHashMap<Class<?>, Set<ResultConvertor<?, ?>>> superResultConvertor;

    /**
     * Description java类型转换成语句时的convertor
     *
     * @author zhaolaiyuan
     * Date 2022/12/4 12:39
     **/
    private final ConcurrentHashMap<Class<?>, StatementConvertor<?>> statementConvertorMap;

    /**
     * Description java类型转换成语句时的convertor，这里是判断是否子类的转换器，挨个试，找到后存到上面的map里
     *
     * @author zhaolaiyuan
     * Date 2022/12/4 12:39
     **/
    private final CopyOnWriteArrayList<StatementConvertor<?>> superClassConvertor;

    public ConvertorManager() {
        this.resultConvertorMap = new TwofoldConcurrentHashMap<>();
        this.superResultConvertor = new ConcurrentHashMap<>();
        this.statementConvertorMap = new ConcurrentHashMap<>();
        this.superClassConvertor = new CopyOnWriteArrayList<>();
    }


    public <T1, T2> void registerResultConvertor(ResultConvertor<T1, T2> resultConvertor) {
        resultConvertorMap.put(resultConvertor.getOriginType(), resultConvertor.getTargetType(), resultConvertor);
    }

    public <T1> void registerStatementConvertor(StatementConvertor<T1> statementConvertor) {
        statementConvertorMap.put(statementConvertor.getOriginType(), statementConvertor);
    }

    public <T1, T2> void registerSuperClassConvertor(StatementConvertor<T1> statementConvertor, ResultConvertor<T2, T1> resultConvertor) {
        this.superClassConvertor.add(statementConvertor);
        this.superResultConvertor.compute(
            resultConvertor.getOriginType(),
            (k, v) -> {
                if (v == null) {
                    v = new CopyOnWriteArraySet<>();
                }
                v.add(resultConvertor);
                return v;
            }
        );
    }

    public <T1, T2> ResultConvertor<T1, T2> getResultConvertor(Class<T1> originType, Class<T2> targetType) {
        ResultConvertor<?, ?> resultConvertor = resultConvertorMap.get(originType, targetType);
        if (resultConvertor != null) {
            return (ResultConvertor<T1, T2>) resultConvertor;
        }
        if (originType.equals(targetType) || targetType.equals(Object.class)) {
            // 结果转换器没有，并且出入参相同，或者目标类型是Object，则直接返回
            return (ResultConvertor<T1, T2>) selfConvertor;
        }
        Set<ResultConvertor<?, ?>> resultConvertors = superResultConvertor.get(originType);
        if (resultConvertors == null) {
            throw ModelDefineException.noMatchResultConvertor(originType, targetType);
        }
        for (ResultConvertor<?, ?> convertor : resultConvertors) {
            if (ClassUtils.isExtend(convertor.getTargetType(), targetType)) {
                return (ResultConvertor<T1, T2>) convertor;
            }
        }
        throw ModelDefineException.noMatchResultConvertor(originType, targetType);
    }

    /**
     * Description 获取转换成语句的convertor
     *
     * @author zhaolaiyuan
     * Date 2022/12/4 12:04
     **/
    public <T1> StatementConvertor<T1> getStatementConvertor(Class<T1> javaType) {
        StatementConvertor<?> statementConvertor = statementConvertorMap.get(javaType);
        if (statementConvertor != null) {
            return (StatementConvertor<T1>) statementConvertor;
        }
        for (StatementConvertor<?> convertor : superClassConvertor) {
            if (ClassUtils.isExtend(convertor.getOriginType(), javaType)) {
                return (StatementConvertor<T1>) convertor;
            }
        }
        throw ModelDefineException.noMatchStatementConvertor(javaType);
    }
}
