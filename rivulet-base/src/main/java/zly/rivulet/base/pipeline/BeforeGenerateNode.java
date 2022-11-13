package zly.rivulet.base.pipeline;


import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.param_manager.ParamManager;

public abstract class BeforeGenerateNode {
    private BeforeGenerateNode next;

    public abstract Object handle(Blueprint blueprint, ParamManager paramManager, ExecutePlan executePlan);

    protected Object nextHandle(Blueprint blueprint, ParamManager paramManager, ExecutePlan executePlan) {
        return next.handle(blueprint, paramManager, executePlan);
    }

    public void setNext(BeforeGenerateNode next) {
        this.next = next;
    }
}
