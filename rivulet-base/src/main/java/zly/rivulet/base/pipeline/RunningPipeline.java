package zly.rivulet.base.pipeline;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.executor.Executor;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.pipeline.toolbox.ExecuteFlag;
import zly.rivulet.base.pipeline.toolbox.PipelineToolbox;
import zly.rivulet.base.utils.ClassUtils;

import java.util.Collection;
import java.util.Objects;

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
        public Object handle(Blueprint blueprint, ParamManager paramManager, PipelineToolbox pipelineToolbox) {
            Fish fish = generator.generate(blueprint, paramManager);
            // 执行
            Object result = beforeExecuteNode.handle(blueprint, fish, pipelineToolbox);
            return afterExecuteNode.handle(fish, result, pipelineToolbox);
        }
    }

    private final class DistributeExecuteNode extends BeforeExecuteNode {
        @Override
        public Object handle(Blueprint blueprint, Fish fish, PipelineToolbox pipelineToolbox) {
            ExecuteFlag executeFlag = pipelineToolbox.getExecuteFlag();
            if (ExecuteFlag.Enum.QUERY_ONE.equals(executeFlag)) {
                // 单个查询
                return executor.queryOne(fish, blueprint.getAssigner(), pipelineToolbox);
            } else if (ExecuteFlag.Enum.QUERY_MANY.equals(executeFlag)) {
                // 批量查询
                return executor.queryList(fish, blueprint.getAssigner(), pipelineToolbox);
            } else {
                // 增删改
                return executor.executeUpdate(fish, pipelineToolbox);
            }
        }
    }

    private final class FinalExecuteNode extends AfterExecuteNode {

        @Override
        public Object handle(Fish fish, Object result, PipelineToolbox pipelineToolbox) {
            return result;
        }
    }

    @FunctionalInterface
    public interface Executor {

        Object exec(Fish fish);
    }
}
