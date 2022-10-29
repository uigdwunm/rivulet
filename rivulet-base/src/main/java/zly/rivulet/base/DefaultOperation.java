package zly.rivulet.base;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.SimpleParamManager;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface DefaultOperation {

    /*********************************************** 解析BluePrint ***********************************************/

    /**
     * 直接加载key，说明是没加载过的，需要临时解析
     **/
    Blueprint parse(WholeDesc wholeDesc);



    /*********************************************** 单个查询 ***********************************************/

    /**
     * 通过预定义desc的key，查询单个
     **/
    <T> T queryOneByDescKey(String descKey, Map<String, Object> params);

    /**
     * 通过blueprint查询单个
     **/
    default <T> T queryOneByBlueprint(Blueprint blueprint, Map<String, Object> params) {
        return this.queryOneByBlueprint(blueprint, new SimpleParamManager(params));
    }

    /**
     * 通过blueprint查询单个DO对象
     **/
    <T> T queryOneByBlueprint(Blueprint blueprint, ParamManager paramManager);

    /**
     * 通过id查询单个DO对象
     **/
    <T, I> T queryById(I id, Class<T> modelClass);



    /*********************************************** 批量查询 ***********************************************/

    /**
     * 通过预定义desc的key，查询多个，返回LinkedList
     **/
    default <T> List<T> queryManyByDescKey(String descKey, Map<String, Object> params) {
        LinkedList<T> list = new LinkedList<>();
        this.queryManyByDescKey(descKey, params, new LinkedList<>());
        return list;
    }

    /**
     * 通过预定义desc的key，查询多个，自传结果容器
     **/
    <T> void queryManyByDescKey(String descKey, Map<String, Object> params, Collection<T> resultContainer);

    /**
     * 通过解析好的blueprint，查询多个
     **/
    default <T> List<T> queryManyByBlueprint(Blueprint blueprint, Map<String, Object> params) {
        List<T> list = new LinkedList<>();
        this.queryManyByBlueprint(blueprint, params, list);
        return list;
    }

    /**
     * 通过解析好的blueprint, 查询多个, 自传结果容器
     **/
    default <T> void queryManyByBlueprint(Blueprint blueprint, Map<String, Object> params, Collection<T> resultContainer) {
        this.queryManyByBlueprint(blueprint, new SimpleParamManager(params), resultContainer);
    }

    /**
     * 通过blueprint查询多个，自带结果容器
     **/
    <T> void queryManyByBlueprint(Blueprint blueprint, ParamManager paramManager, Collection<T> resultContainer);

    /**
     * 通过传入的idList，查询DO模型列表
     **/
    <T, I> List<T> queryByIds(Collection<I> ids, Class<T> modelClass);



    /*********************************************** 新增 ***********************************************/

    /**
     * 单个新增
     **/
    <T> int insertOne(T obj);

    /**
     * 批量新增
     **/
    <T> int[] batchInsert(Collection<T> batchModel, Class<T> dOModelClass);



    /*********************************************** 更新 ***********************************************/

    /**
     * 通过主键id更新
     **/
    <T> int updateOneById(T obj);

    /**
     * 批量通过主键id更新
     **/
    <T> int batchUpdateById(Collection<T> obj);

    /**
     * 通过预解析desc的key, 更新
     **/
    // 通过desc更新
    int updateByDescKey(String descKey, Map<String, Object> params);

    /**
     * 通过解析好的Blueprint更新
     **/
    // 通过desc更新
    int updateByBlueprint(Blueprint blueprint, Map<String, Object> params);



    /*********************************************** 删除 ***********************************************/

    /**
     * 通过id删除单个
     **/
    <T, I> int deleteById(I id, Class<T> modelClass);

    /**
     * 通过id删除多个
     **/
    <T, I> int deleteByIds(Collection<I> ids, Class<T> modelClass);

    /**
     * 通过解析好的blueprint删除
     **/
    //
    int deleteByBlueprint(Blueprint blueprint, Map<String, Object> params);
}
