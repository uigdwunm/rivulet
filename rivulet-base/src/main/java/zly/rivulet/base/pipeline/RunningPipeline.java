package zly.rivulet.base.pipeline;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.executor.Executor;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.utils.ClassUtils;

import java.util.Collection;

public class RunningPipeline {

    private BeforeGenerateNode beforeGenerateNode;

    private BeforeExecuteNode beforeExecuteNode;

    private AfterExecuteNode afterExecuteNode;

    private final Generator generator;

    private final Executor executor;

    public RunningPipeline(Generator generator, Executor executor) {
        this.generator = generator;
        this.executor = executor;
        this.beforeGenerateNode = new FinalGenerateNode();
        this.beforeExecuteNode = new DistributeExecuteNode();
        this.afterExecuteNode = new FinalExecuteNode();
    }

    public void warmUp(Blueprint definition) {
        generator.warmUp(definition);
    }

    public Object go(Blueprint blueprint, ParamManager paramManager, Class<?> returnType) {
        return beforeGenerateNode.handle(blueprint, paramManager, returnType);
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
        public Object handle(Blueprint blueprint, ParamManager paramManager, Class<?> returnType) {
            Fish fish = generator.generate(blueprint, paramManager);
            Object result = beforeExecuteNode.handle(blueprint, fish, returnType);
            return afterExecuteNode.handle(fish, returnType, result);
        }
    }

    private final class DistributeExecuteNode extends BeforeExecuteNode {
        @Override
        public Object handle(Blueprint blueprint, Fish fish, Class<?> returnType) {
            // 是查询方法
            if (ClassUtils.isExtend(Collection.class, returnType)) {
                // 返回值是集合
                return executor.queryList(fish, blueprint.getAssigner());
            } else {
                return executor.queryOne(fish, blueprint.getAssigner());
            }

            // TODO 判断是batch类型的fish，批量插入、批量更新,不在这里判断，在mysql的executor中判断，只是先写在这里
            // TODO 事物也是executor中的概念，暂时不考虑在前面的流程中控制
        }
    }

    private final class FinalExecuteNode extends AfterExecuteNode {

        @Override
        public Object handle(Fish fish, Class<?> returnType, Object result) {
            return result;
        }
    }
}
