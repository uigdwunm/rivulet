package zly.rivulet.base.pipeline;


import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.pipeline.toolbox.PipelineToolbox;

public abstract class BeforeGenerateNode {
    BeforeGenerateNode next;

    public abstract Object handle(Blueprint blueprint, ParamManager paramManager, RunningPipeline.Executor executor);

    protected Object nextHandle(Blueprint blueprint, ParamManager paramManager, RunningPipeline.Executor executor) {
        return next.handle(blueprint, paramManager, executor);
    }

    void setNext(BeforeGenerateNode next) {
        this.next = next;
    }
}
