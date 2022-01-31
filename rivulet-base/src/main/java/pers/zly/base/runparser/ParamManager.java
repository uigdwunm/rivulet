package pers.zly.base.runparser;

import pers.zly.base.definition.param.ParamDefinition;

import java.util.HashMap;
import java.util.Map;

public class ParamManager {

    /**
     * Description 原始参数
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 12:15
     **/
    private final Object[] originParam;

    /**
     * Description 每次查询都有的，所以不用并发
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 12:16
     **/
    private final Map<ParamDefinition, Object> cache = new HashMap<>();

    public ParamManager(Object[] originParam) {
        this.originParam = originParam;
    }

    public Object getParam(ParamDefinition paramDefinition) {
        Object param = cache.get(paramDefinition);
        if (param != null) {
            return param;
        }

        param = paramDefinition.getParam(originParam);
        cache.put(paramDefinition, param);

        return param;
    }

    public String getStatement(ParamDefinition paramDefinition) {
        Object param = this.getParam(paramDefinition);
        return paramDefinition.getConverter().convertToStatement(param);
    }

}
