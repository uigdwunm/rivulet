package zly.rivulet.base.pipeline;


import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;

public abstract class BeforeExecuteNode {
    BeforeExecuteNode next;

    public abstract Object handle(Blueprint blueprint, Fish fish, Executor executor);

    protected Object nextHandle(Blueprint blueprint, Fish fish, Executor executor) {
        return next.handle(blueprint, fish, executor);
    }

    void setNext(BeforeExecuteNode next) {
        this.next = next;
    }
}
