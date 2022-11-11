package zly.rivulet.base.pipeline;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;

import java.sql.Connection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ExecutePlan {

    private RunningPipeline runningPipeline;

    public abstract Object plan(Blueprint blueprint, ParamManager paramManager, Class<?> returnType, Connection connection);

    final void setRunningPipeline(RunningPipeline runningPipeline) {
        this.runningPipeline = runningPipeline;
    }

    protected final Generator getGenerator() {
        return runningPipeline.generator;
    }

    /**
     * Description 进入流水线的下一环 -- 执行前(包括执行)
     *
     * @author zhaolaiyuan
     * Date 2022/11/8 8:58
     **/
    protected final Object beforeAndExecute(Fish fish, Supplier<Object> executor) {
        Object result = runningPipeline.beforeExecuteNode.handle(fish, executor);
        return runningPipeline.afterExecuteNode.handle(fish, result);
    }
}
