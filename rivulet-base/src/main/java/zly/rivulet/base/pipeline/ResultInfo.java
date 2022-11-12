package zly.rivulet.base.pipeline;

import java.util.Collection;

public class ResultInfo {

    private final Class<?> resultType;

    private Object resultContainer;

    private ResultInfo(Class<?> resultType, Object resultContainer) {
        this.resultType = resultType;
        this.resultContainer = resultContainer;
    }

    public static ResultInfo of(Class<? extends Collection<?>> resultType, Collection<Object> resultContainer) {
        return new ResultInfo(resultType, resultContainer);
    }

    public static ResultInfo of(Class<?> resultType, Object resultContainer) {
        return new ResultInfo(resultType, resultContainer);
    }

    public static ResultInfo of(Class<?> resultType) {
        return new ResultInfo(resultType, null);
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public Object getResultContainer() {
        return resultContainer;
    }

    public void setResultContainer(Object resultContainer) {
        this.resultContainer = resultContainer;
    }
}
