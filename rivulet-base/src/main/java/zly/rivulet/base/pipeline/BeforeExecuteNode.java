package zly.rivulet.base.pipeline;


import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.pipeline.toolbox.PipelineToolbox;

public abstract class BeforeExecuteNode {
    BeforeExecuteNode next;

    public abstract Object handle(Blueprint blueprint, Fish fish, PipelineToolbox pipelineToolbox);

    protected Object nextHandle(Blueprint blueprint, Fish fish, PipelineToolbox pipelineToolbox) {
        return next.handle(blueprint, fish, pipelineToolbox);
    }

    void setNext(BeforeExecuteNode next) {
        this.next = next;
    }
}
