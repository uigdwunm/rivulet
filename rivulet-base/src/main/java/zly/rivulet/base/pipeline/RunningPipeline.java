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
    private BeforeDistributeNode beforeDistributeNode;

    protected BeforeExecuteNode beforeExecuteNode;

    protected AfterExecuteNode afterExecuteNode;

    public RunningPipeline(Generator generator) {
        this.generator = generator;
        this.beforeExecuteNode = new FinalExecuteNode();
        this.afterExecuteNode = new FinishedNode();
    }

    public Object go(Blueprint blueprint, ParamManager paramManager, ResultInfo resultInfo, Connection connection) {
        return beforeDistributeNode.handle(blueprint, paramManager, resultInfo, connection);
    }

    public void addBeforeGenerateNode(BeforeDistributeNode beforeDistributeNode) {
        beforeDistributeNode.setNext(this.beforeDistributeNode);
        this.beforeDistributeNode = beforeDistributeNode;
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
