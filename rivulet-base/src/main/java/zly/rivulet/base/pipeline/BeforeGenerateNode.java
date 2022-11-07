package zly.rivulet.base.pipeline;


import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.pipeline.toolbox.PipelineToolbox;

import java.sql.Connection;

public abstract class BeforeGenerateNode {
    BeforeGenerateNode next;

    public abstract Object handle(Blueprint blueprint, ParamManager paramManager, Class<?> returnType, Connection connection);

    protected Object nextHandle(Blueprint blueprint, ParamManager paramManager, Class<?> returnType, Connection connection) {
        return next.handle(blueprint, paramManager, returnType, connection);
    }

    void setNext(BeforeGenerateNode next) {
        this.next = next;
    }
}
