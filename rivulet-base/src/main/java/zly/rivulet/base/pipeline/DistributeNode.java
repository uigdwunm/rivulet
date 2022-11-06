package zly.rivulet.base.pipeline;

import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.utils.TwofoldConcurrentHashMap;

public class DistributeNode extends BeforeGenerateNode {

    private final Generator generator;

    protected BeforeExecuteNode beforeExecuteNode;

    protected AfterExecuteNode afterExecuteNode;

    private TwofoldConcurrentHashMap<RivuletFlag, Class<?>, > map = new TwofoldConcurrentHashMap<>();

    public DistributeNode(Generator generator) {
        this.generator = generator;
        this.beforeExecuteNode = new RunningPipeline.FinalExecuteNode();
    }

    @Override
    public Object handle(Blueprint blueprint, ParamManager paramManager, Class<?> returnType) {
        return null;
    }
}
