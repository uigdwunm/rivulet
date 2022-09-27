package zly.rivulet.base;

import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.SimpleParamManager;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface DefaultOperation {

    /**
     * 通过desc查询单个
     **/
    default <T> T queryOneByDesc(WholeDesc wholeDesc, Map<String, Object> params) {
        return this.queryOneByDesc(wholeDesc, new SimpleParamManager(params));
    }
    <T> T queryOneByDesc(WholeDesc wholeDesc, ParamManager paramManager);
    // 通过desc查询多个
    default <T> List<T> queryManyByDesc(WholeDesc wholeDesc, Map<String, Object> params) {
        return queryManyByDesc(wholeDesc, params, new LinkedList<>());
    }
    // 通过desc查询多个
    default <T> List<T> queryManyByDesc(WholeDesc wholeDesc, Map<String, Object> params, Collection<T> resultContainer) {
        List<T> list = new LinkedList<>();
        SimpleParamManager simpleParamManager = new SimpleParamManager(params);
        this.queryManyByDesc(wholeDesc, simpleParamManager, resultContainer);
        return list;
    }
    // 通过desc查询多个，自带结果容器
    <T> void queryManyByDesc(WholeDesc wholeDesc, ParamManager paramManager, Collection<T> resultContainer);
    // 通过查询查询单个
    <T, I> T queryOneById(I id, Class<T> modelClass);
    // 通过id查询多个
    <T, I> List<T> queryByIds(Collection<I> ids, Class<T> modelClass);

    // 新增
    <T> int insertOne(T obj);
    // 批量新增
    <T> int batchInsert(Collection<T> batchModel);

    // 通过主键id更新
    <T> int updateOneById(T obj);
    // 批量通过主键id更新
    <T> int batchUpdateById(Collection<T> obj);
    // 通过desc更新
    int updateByDesc(WholeDesc wholeDesc, Map<String, Object> params);


    // 通过id删除单个
    <T, I> int deleteById(I id, Class<T> modelClass);
    // 通过id删除多个
    <T, I> int deleteByIds(Collection<I> ids, Class<T> modelClass);
    // 通过desc删除
    int deleteByDesc(WholeDesc wholeDesc, Map<String, Object> params);
}
