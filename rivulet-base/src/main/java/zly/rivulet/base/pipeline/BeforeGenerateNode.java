package zly.rivulet.base.pipeline;


import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.param_manager.ParamManager;

public abstract class BeforeGenerateNode {
    BeforeGenerateNode next;

    public abstract Object handle(Blueprint blueprint, ParamManager paramManager, Class<?> returnType);

    protected Object nextHandle(Blueprint blueprint, ParamManager paramManager, Class<?> returnType) {
        return next.handle(blueprint, paramManager, returnType);
    }

    void setNext(BeforeGenerateNode next) {
        this.next = next;
    }
}
