package zly.rivulet.base.pipeline;

import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;

import java.sql.Connection;
import java.util.function.Supplier;

public class RunningPipeline {

    protected final Generator generator;
    private BeforeGenerateNode beforeGenerateNode;

    protected BeforeExecuteNode beforeExecuteNode;

    protected AfterExecuteNode afterExecuteNode;

    private final DistributePivot distributePivot;

    public RunningPipeline(Generator generator, DistributePivot distributePivot) {
        this.generator = generator;
        this.beforeGenerateNode = new FinalGenerateNode();
        this.beforeExecuteNode = new FinalExecuteNode();
        this.afterExecuteNode = new FinishedNode();
        this.distributePivot = distributePivot;
    }

    public Object go(Blueprint blueprint, ParamManager paramManager, Class<?> returnType, Connection connection) {
        return beforeGenerateNode.handle(blueprint, paramManager, returnType, connection);
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

    public void registerExecutePlan(RivuletFlag rivuletFlag, Class<?> returnType, ExecutePlan executePlan) {
        executePlan.setRunningPipeline(this);
        this.distributePivot.registerExecutePlan(rivuletFlag, returnType, executePlan);
    }

    private class FinalGenerateNode extends BeforeGenerateNode {
        @Override
        public Object handle(Blueprint blueprint, ParamManager paramManager, Class<?> returnType, Connection connection) {
            // 进入分发，并执行
            return distributePivot.distribute(blueprint, paramManager, returnType, connection);
        }
    }

    private final class FinalExecuteNode extends BeforeExecuteNode {

        @Override
        public Object handle(Fish fish, Supplier<Object> executor) {
            return executor.get();
        }
    }

    private final class FinishedNode extends AfterExecuteNode {
        @Override
        public Object handle(Fish fish, Object result) {
            return result;
        }
    }

}
