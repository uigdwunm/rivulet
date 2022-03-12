package zly.rivulet.base.executor;

import zly.rivulet.base.runparser.Fish;
import zly.rivulet.base.mapper.MapDefinition;

public interface Executor {

    /**
     * Description 真正执行的方法，有可能返回值单值、Stream流
     *
     * @author zhaolaiyuan
     * Date 2021/12/5 11:52
     **/
    Object execute(Fish fish, MapDefinition mapDefinition);

}
