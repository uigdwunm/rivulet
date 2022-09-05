package zly.rivulet.base.pipeline;


import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;

public abstract class BeforeExecuteNode {
    BeforeExecuteNode next;

    public abstract Object handle(Blueprint blueprint, Fish fish, Class<?> returnType);

    protected Object nextHandle(Blueprint blueprint, Fish fish, Class<?> returnType) {
        return next.handle(blueprint, fish, returnType);
    }

    void setNext(BeforeExecuteNode next) {
        this.next = next;
    }
}
