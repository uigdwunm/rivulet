package zly.rivulet.base.runparser.param_manager;

import zly.rivulet.base.definition.param.ParamDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * Description 观察下，没用就干掉
 *
 * @author zhaolaiyuan
 * Date 2022/7/17 15:38
 **/
@Deprecated
public class StaticParamManager implements ParamManager {

    /**
     * Description 每次查询都有的，所以不用并发
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 12:16
     **/
    private final Map<String, Object> cache = new HashMap<>();

    @Override
    public Object getParam(ParamDefinition paramDefinition) {
        return null;
    }

    @Override
    public String getStatement(ParamDefinition paramDefinition) {
        return null;
    }
}
