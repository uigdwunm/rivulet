package zly.rivulet.base.preparser;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.mapper.MapDefinition;

import java.lang.reflect.Method;

/**
 * Description Definition 和 对应的代理方法，会有一些额外的信息，我起不出名字了
 *
 * @author zhaolaiyuan
 * Date 2022/2/1 11:03
 **/
public abstract class Rock {

    private final AbstractDefinition definition;

    private final MapDefinition mapDefinition;

    private final Method proxyMethod;

    protected Rock(AbstractDefinition definition, MapDefinition mapDefinition, Method proxyMethod) {
        this.definition = definition;
        this.mapDefinition = mapDefinition;
        this.proxyMethod = proxyMethod;
    }

    public AbstractDefinition getDefinition() {
        return definition;
    }

    public MapDefinition getMapDefinition() {
        return mapDefinition;
    }
}
