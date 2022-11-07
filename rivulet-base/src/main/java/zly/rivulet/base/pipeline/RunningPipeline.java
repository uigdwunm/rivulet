package zly.rivulet.base.pipeline;

import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_model_meta.ModelBatchParamManager;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.TwofoldConcurrentHashMap;

import java.sql.Connection;
import java.util.Collection;

public class RunningPipeline {

    private final Generator generator;
    private BeforeGenerateNode beforeGenerateNode;

    protected BeforeExecuteNode beforeExecuteNode;

    protected AfterExecuteNode afterExecuteNode;

    private final DistributeNode distributeNode;

    public RunningPipeline(Generator generator) {
        this.generator = generator;
        this.distributeNode = new DistributeNode();
        this.beforeGenerateNode = this.distributeNode;
        this.beforeExecuteNode = new FinalExecuteNode();
        this.afterExecuteNode = new FinishedNode();
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

    public class DistributeNode extends BeforeGenerateNode {

        private TwofoldConcurrentHashMap<RivuletFlag, Class<?>, BeforeGenerateNode> map = new TwofoldConcurrentHashMap<>();

        @Override
        public Object handle(Blueprint blueprint, ParamManager paramManager, Class<?> returnType, Connection connection) {
            RivuletFlag flag = blueprint.getFlag();
            BeforeGenerateNode generateNode = map.get(flag, returnType);
            if (generateNode != null) {
                // 自定义了中转节点的
                return generateNode.handle(blueprint, paramManager, returnType, connection);
            }

            if (RivuletFlag.QUERY.equals(flag)) {
                if (ClassUtils.isExtend(Collection.class, returnType)) {
                    // 批量查询
                    return ;
                } else {
                    // 单个查询
                    return ;
                }
            } else if (RivuletFlag.INSERT.equals(flag) && paramManager instanceof ModelBatchParamManager) {
                // 批量插入
                return ;
            } else {
                // 普通插入、更新、删除
                return ;
            }
        }
    }

    protected final class FinalExecuteNode extends BeforeExecuteNode {

        @Override
        public Object handle(Blueprint blueprint, Fish fish, Executor executor) {
            return executor.exec(fish);
        }
    }

    protected final class FinishedNode extends AfterExecuteNode {
        @Override
        public Object handle(Fish fish, Object result) {
            return result;
        }
    }

}
