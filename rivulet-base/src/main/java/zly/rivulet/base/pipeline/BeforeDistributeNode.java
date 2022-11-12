package zly.rivulet.base.pipeline;


import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.param_manager.ParamManager;

import java.sql.Connection;

public abstract class BeforeDistributeNode {
    BeforeDistributeNode next;

    public abstract Object handle(Blueprint blueprint, ParamManager paramManager, ResultInfo resultInfo, Connection connection);

    protected Object nextHandle(Blueprint blueprint, ParamManager paramManager, ResultInfo resultInfo, Connection connection) {
        return next.handle(blueprint, paramManager, resultInfo, connection);
    }

    void setNext(BeforeDistributeNode next) {
        this.next = next;
    }
}
