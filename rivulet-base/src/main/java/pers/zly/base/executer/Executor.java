package pers.zly.base.executer;

import pers.zly.base.runparser.statement.Statement;
import pers.zly.base.mapper.MapDefinition;

import java.util.stream.Stream;

public interface Executor {

    /**
     * Description 真正执行的方法，返回批量值
     *
     * @author zhaolaiyuan
     * Date 2021/12/5 11:52
     **/
    Stream<?> executeReturnBatch(Statement statement, MapDefinition mapDefinition);

    /**
     * Description 真正执行的方法，返回单值，对象之类的
     *
     * @author zhaolaiyuan
     * Date 2021/12/5 11:52
     **/
    Object executeReturnOne(Statement statement, MapDefinition mapDefinition);

}
