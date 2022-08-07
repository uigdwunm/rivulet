package zly.rivulet.base.executor;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.assembly_line.Fish;

import java.util.stream.Stream;

public interface Executor {

    /**
     * 单个查询
     **/
    Object queryOne(Fish fish, Assigner<?> assigner);

    /**
     * 批量查询
     **/
    Stream<Object> queryList(Fish fish, Assigner<?> assigner);

    /**
     * 增删改
     **/
    int executeUpdate(Fish fish);

}
