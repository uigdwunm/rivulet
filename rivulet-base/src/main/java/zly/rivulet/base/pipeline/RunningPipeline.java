package zly.rivulet.base.pipeline;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;

import java.util.function.Supplier;

public class RunningPipeline {

    protected final Generator generator;

    private BeforeGenerateNode beforeGenerateNode;

    protected BeforeExecuteNode beforeExecuteNode;

    protected AfterExecuteNode afterExecuteNode;


    public RunningPipeline(Generator generator) {
        this.generator = generator;
        this.beforeExecuteNode = new FinalExecuteNode();
        this.afterExecuteNode = new FinishedNode();
    }

    public <T> T go(Blueprint blueprint, ParamManager paramManager, ExecutePlan executePlan) {
        return (T) beforeGenerateNode.handle(blueprint, paramManager, executePlan);
    }

    public void addBeforeDistributeNode(BeforeGenerateNode beforeGenerateNode) {
        beforeGenerateNode.setNext(this.beforeGenerateNode);
        this.beforeGenerateNode = beforeGenerateNode;
    }

    public void addBeforeExecuteNode(BeforeExecuteNode beforeExecuteNode) {
        beforeExecuteNode.setNext(this.beforeExecuteNode);
        this.beforeExecuteNode = beforeExecuteNode;
    }

    public void addAfterExecuteNode(AfterExecuteNode afterExecuteNode) {
        afterExecuteNode.setNext(this.afterExecuteNode);
        this.afterExecuteNode = afterExecuteNode;
    }

    private final class FinalGenerateNode extends BeforeGenerateNode {

        @Override
        public Object handle(Blueprint blueprint, ParamManager paramManager, ExecutePlan executePlan) {
            return executePlan.plan(blueprint, paramManager, generator, beforeExecuteNode);
        }
    }

    private final class FinalExecuteNode extends BeforeExecuteNode {

        @Override
        public Object handle(Fish fish, Supplier<Object> executor) {
            Object result = executor.get();
            return afterExecuteNode.handle(fish, result);
        }
    }

    private final class FinishedNode extends AfterExecuteNode {
        @Override
        public Object handle(Fish fish, Object result) {
            return result;
        }
    }

}
