package zly.rivulet.base.pipeline;

import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;

public class ExecuteDistributeFactory {
    private final Generator generator;
    private final BeforeExecuteNode executeNode;

    private TwofoldConcurrentHashMap<RivuletFlag, Class<?>, > map;

    public ExecuteDistributeFactory(Generator generator, BeforeExecuteNode beforeExecuteNode) {
        this.generator = generator;
        this.executeNode = beforeExecuteNode;
    }

    public Object exec(Blueprint blueprint, ParamManager paramManager, Class<?> returnType) {
        Fish fish = generator.generate(blueprint, paramManager);
        // 执行
        return executeNode.handle(blueprint, fish, executor);
    }
}
