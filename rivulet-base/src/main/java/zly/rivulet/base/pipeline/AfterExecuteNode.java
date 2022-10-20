package zly.rivulet.base.pipeline;


import zly.rivulet.base.generator.Fish;

public abstract class AfterExecuteNode {
    AfterExecuteNode next;

    public abstract Object handle(Fish fish, Object result);


    protected Object nextHandle(Fish fish, Object result) {
        return next.handle(fish, result);
    }

    void setNext(AfterExecuteNode next) {
        this.next = next;
    }
}
