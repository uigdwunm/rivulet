package zly.rivulet.base.pipeline;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;

public class RunningPipeline {

    private BeforeGenerateNode beforeGenerateNode;

    private BeforeExecuteNode beforeExecuteNode;

    private AfterExecuteNode afterExecuteNode;

    private final Generator generator;

    public RunningPipeline(Generator generator) {
        this.generator = generator;
        this.beforeGenerateNode = new FinalGenerateNode();
        this.beforeExecuteNode = new DistributeExecuteNode();
        this.afterExecuteNode = new FinalExecuteNode();
    }

    public Object go(Blueprint blueprint, ParamManager paramManager, Executor executor) {
        return beforeGenerateNode.handle(blueprint, paramManager, executor);
    }

    public void addBeforeGenerateNode(BeforeGenerateNode beforeGenerateNode) {
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
        public Object handle(Blueprint blueprint, ParamManager paramManager, RunningPipeline.Executor executor) {
            Fish fish = generator.generate(blueprint, paramManager);
            // 执行
            Object result = beforeExecuteNode.handle(blueprint, fish, executor);
            return afterExecuteNode.handle(fish, result);
        }
    }

    private final class DistributeExecuteNode extends BeforeExecuteNode {
        @Override
        public Object handle(Blueprint blueprint, Fish fish, RunningPipeline.Executor executor) {
            return executor.exec(fish);
        }
    }

    private final class FinalExecuteNode extends AfterExecuteNode {
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
