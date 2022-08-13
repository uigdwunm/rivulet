package zly.rivulet.base.pipeline;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.executor.Executor;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;

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
        this.generator.warmUp(definition);
    }

    public Object go(Blueprint blueprint, ParamManager paramManager, Class<?> returnType) {
        return beforeGenerateNode.handle(blueprint, paramManager, returnType);
    }

    public Object execute(Blueprint blueprint, Fish fish, Class<?> returnType) {
        Object result = beforeExecuteNode.handle(blueprint, fish, returnType);

        return afterExecuteNode.handle(fish, returnType, result);
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

    public abstract static class BeforeGenerateNode {
        protected BeforeGenerateNode next;

        public Object handle(Blueprint blueprint, ParamManager paramManager, Class<?> returnType) {
            return next.handle(blueprint, paramManager, returnType);
        }

        private void setNext(BeforeGenerateNode next) {
            this.next = next;
        }
    }

    private class FinalGenerateNode extends BeforeGenerateNode {

        @Override
        public Object handle(Blueprint blueprint, ParamManager paramManager, Class<?> returnType) {
            Fish fish = generator.generate(blueprint, paramManager);
            return execute(blueprint, fish, returnType);
        }
    }

    public abstract static class BeforeExecuteNode {
        protected BeforeExecuteNode next;

        public Object handle(Blueprint blueprint, Fish fish, Class<?> returnType) {
            return next.handle(blueprint, fish, returnType);
        }

        private void setNext(BeforeExecuteNode next) {
            this.next = next;
        }
    }

    public class DistributeExecuteNode extends BeforeExecuteNode {
        @Override
        public Object handle(Blueprint blueprint, Fish fish, Class<?> returnType) {
            // 是查询方法
            if (Collection.class.isAssignableFrom(returnType)) {
                // 返回值是集合
                return executor.queryList(fish, blueprint.getAssigner());
            } else {
                return executor.queryOne(fish, blueprint.getAssigner());
            }
        }
    }

    public abstract static class AfterExecuteNode {
        protected AfterExecuteNode next;

        public Object handle(Fish fish, Class<?> returnType, Object result) {
            return next.handle(fish, returnType, result);
        }

        private void setNext(AfterExecuteNode next) {
            this.next = next;
        }
    }

    private class FinalExecuteNode extends AfterExecuteNode {

        @Override
        public Object handle(Fish fish, Class<?> returnType, Object result) {
            return result;
        }
    }
}
