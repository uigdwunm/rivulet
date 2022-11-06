package zly.rivulet.base.pipeline;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;

public class RunningPipeline {

    private BeforeGenerateNode beforeGenerateNode;

    private final DistributeNode distributeNode;

    public RunningPipeline(DistributeNode distributeNode) {
        this.beforeGenerateNode = distributeNode;
        this.distributeNode = distributeNode;
    }

    public Object go(Blueprint blueprint, ParamManager paramManager, Class<?> returnType) {
        return beforeGenerateNode.handle(blueprint, paramManager, returnType);
    }

    public void addBeforeGenerateNode(BeforeGenerateNode beforeGenerateNode) {
        beforeGenerateNode.setNext(this.beforeGenerateNode);
        this.beforeGenerateNode = beforeGenerateNode;
    }

    public void addBeforeExecuteNode(BeforeExecuteNode beforeExecuteNode) {
        beforeExecuteNode.setNext(distributeNode.beforeExecuteNode);
        distributeNode.beforeExecuteNode = beforeExecuteNode;
    }

    public void addAfterExecuteNode(AfterExecuteNode afterExecuteNode) {
        afterExecuteNode.setNext(distributeNode.afterExecuteNode);
        distributeNode.afterExecuteNode = afterExecuteNode;
    }

    protected final class FinalExecuteNode extends BeforeExecuteNode {

        @Override
        public Object handle(Blueprint blueprint, Fish fish, RunningPipeline.Executor executor) {
            return executor.exec(fish);
        }
    }

    protected final class FinishedNode extends AfterExecuteNode {
        @Override
        public Object handle(Fish fish, Object result) {
            return result;
        }
    }

    @FunctionalInterface
    public interface Executor {
        Object exec(Fish fish);
    }
}
