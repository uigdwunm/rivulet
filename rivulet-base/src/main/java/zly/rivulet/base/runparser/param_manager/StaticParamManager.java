package zly.rivulet.base.runparser.param_manager;

import zly.rivulet.base.definition.param.ParamDefinitionSQL;

import java.util.HashMap;
import java.util.Map;

public class StaticParamManager implements ParamManager {

    /**
     * Description 每次查询都有的，所以不用并发
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 12:16
     **/
    private final Map<String, Object> cache = new HashMap<>();

    @Override
    public Object getParam(ParamDefinitionSQL paramDefinition) {
        return null;
    }

    @Override
    public String getStatement(ParamDefinitionSQL paramDefinition) {
        return null;
    }
}
