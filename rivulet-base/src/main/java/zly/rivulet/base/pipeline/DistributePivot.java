package zly.rivulet.base.pipeline;

import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.utils.TwofoldConcurrentHashMap;

import java.sql.Connection;

public abstract class DistributePivot {

    private TwofoldConcurrentHashMap<RivuletFlag, Class<?>, ExecutePlan> executePlanMap = new TwofoldConcurrentHashMap<>();

    public abstract Object distribute(Blueprint blueprint, ParamManager paramManager, ResultInfo resultInfo, Connection connection);

    public void registerExecutePlan(RivuletFlag rivuletFlag, Class<?> returnType, ExecutePlan executePlan) {
        this.executePlanMap.put(rivuletFlag, returnType, executePlan);
    }

}
