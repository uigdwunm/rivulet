package zly.rivulet.base.pipeline;


import zly.rivulet.base.generator.Fish;

public abstract class AfterExecuteNode {
    AfterExecuteNode next;

    public abstract Object handle(Fish fish, Object result, PipelineToolbox pipelineToolbox);


    protected Object nextHandle(Fish fish, Object result, PipelineToolbox pipelineToolbox) {
        return next.handle(fish, result, pipelineToolbox);
    }

    void setNext(AfterExecuteNode next) {
        this.next = next;
    }
}
