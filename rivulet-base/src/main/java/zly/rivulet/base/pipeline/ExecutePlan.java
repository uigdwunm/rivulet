package zly.rivulet.base.pipeline;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;

public abstract class ExecutePlan {

    public abstract Object plan(Blueprint blueprint, ParamManager paramManager, Generator generator, BeforeExecuteNode beforeExecuteNode);
}
