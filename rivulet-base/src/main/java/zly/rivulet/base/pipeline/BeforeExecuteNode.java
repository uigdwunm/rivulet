package zly.rivulet.base.pipeline;


import zly.rivulet.base.generator.Fish;

import java.util.function.Supplier;

public abstract class BeforeExecuteNode {
    BeforeExecuteNode next;

    public abstract Object handle(Fish fish, Supplier<Object> executor);

    protected Object nextHandle(Fish fish, Supplier<Object> executor) {
        return next.handle(fish, executor);
    }

    void setNext(BeforeExecuteNode next) {
        this.next = next;
    }
}
