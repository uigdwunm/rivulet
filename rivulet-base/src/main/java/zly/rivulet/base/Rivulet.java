package zly.rivulet.base;

import zly.rivulet.base.definer.Definer;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.definition.param.ParamManagerFactory;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.SimpleParamManager;
import zly.rivulet.base.parser.Parser;
import zly.rivulet.base.pipeline.ExecutePlan;
import zly.rivulet.base.pipeline.RunningPipeline;
import zly.rivulet.base.utils.CollectionInstanceCreator;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.warehouse.WarehouseManager;

import java.lang.reflect.Method;
import java.util.*;

public abstract class Rivulet {

    protected final Parser parser;

    protected final Definer definer;

    protected final RunningPipeline runningPipeline;

    protected final ParamManagerFactory paramManagerFactory;

    protected final CollectionInstanceCreator collectionInstanceCreator;

    protected final RivuletProperties rivuletProperties;

    protected final WarehouseManager warehouseManager;

    protected Rivulet(RivuletManager rivuletManager) {
        this.parser = rivuletManager.parser;
        this.definer = rivuletManager.definer;
        this.runningPipeline = rivuletManager.runningPipeline;
        this.paramManagerFactory = rivuletManager.paramManagerFactory;
        this.collectionInstanceCreator = rivuletManager.collectionInstanceCreator;
        this.rivuletProperties = rivuletManager.configProperties;
        this.warehouseManager = rivuletManager.warehouseManager;
    }

    public Blueprint parse(WholeDesc wholeDesc) {
        return parser.parse(wholeDesc);
    }

    /**
     * Description 代理方法中调用的
     *
     * @author zhaolaiyuan
     * Date 2022/12/25 12:19
     **/
    public Object exec(Method proxyMethod, Object[] args) {
        Blueprint sqlBlueprint = warehouseManager.getByProxyMethod(proxyMethod);
        ParamManager paramManager = paramManagerFactory.getByProxyMethod(sqlBlueprint, proxyMethod, args);
        // 执行
        ExecutePlan executePlan = this.createExecutePlan(sqlBlueprint.getFlag(), proxyMethod.getReturnType());
        return runningPipeline.go(sqlBlueprint, paramManager, executePlan);
    }

    protected abstract ExecutePlan createExecutePlan(RivuletFlag rivuletFlag, Class<?> returnType);

    /*-------------------------------------------------- 单个查询 --------------------------------------------------*/

    /**
     * 通过预定义desc的key，查询单个
     **/
    public <T> T queryOneByDescKey(String descKey, Map<String, Object> params) {
        // 解析definition
        Blueprint blueprint = warehouseManager.getByDescKey(descKey);
        return this.queryOneByBlueprint(blueprint, new SimpleParamManager(params));
    }

    /**
     * 通过id查询单个DO对象
     **/
    public <T, I> T queryById(I id, Class<T> modelClass) {
        ModelMeta modelMeta = definer.createOrGetModelMeta(modelClass);
        Blueprint blueprint = this.parser.parseSelectByMeta(modelMeta);
        return this.queryOneByBlueprint(blueprint, Collections.singletonMap(Constant.MAIN_ID, id));
    }

    /**
     * 通过blueprint查询单个
     **/
    public  <T> T queryOneByBlueprint(Blueprint blueprint, Map<String, Object> params) {
        return this.queryOneByBlueprint(blueprint, new SimpleParamManager(params));
    }

    /**
     * 通过blueprint查询单个DO对象
     **/
    public abstract <T> T queryOneByBlueprint(Blueprint blueprint, ParamManager paramManager);



    /*********************************************** 批量查询 ***********************************************/

    /**
     * 通过预定义desc的key，查询多个，返回LinkedList
     **/
    public  <T> List<T> queryManyByDescKey(String descKey, Map<String, Object> params) {
        LinkedList<T> list = new LinkedList<>();
        this.queryManyByDescKey(descKey, params, new LinkedList<>());
        return list;
    }

    /**
     * 通过预定义desc的key，查询多个，自传结果容器
     **/
    public <T> void queryManyByDescKey(String descKey, Map<String, Object> params, Collection<T> resultContainer) {
        // 解析definition
        Blueprint blueprint = warehouseManager.getByDescKey(descKey);
        this.queryManyByBlueprint(blueprint, params, resultContainer);
    }

    /**
     * 通过传入的idList，查询DO模型列表
     **/
    public <T, I> List<T> queryByIds(Collection<I> ids, Class<T> modelClass) {
        List<T> list = new LinkedList<>();
        this.queryByIds(ids, modelClass, list);
        return list;
    }

    /**
     * 通过传入的idList，查询DO模型列表
     **/
    public <T, I> void queryByIds(Collection<I> ids, Class<T> modelClass, Collection<T> resultContainer) {
        ModelMeta modelMeta = definer.createOrGetModelMeta(modelClass);
        Blueprint blueprint = this.parser.parseSelectByMeta(modelMeta);
        this.queryManyByBlueprint(blueprint, Collections.singletonMap(Constant.MAIN_IDS, ids), resultContainer);
    }

    /**
     * 通过解析好的blueprint，查询多个
     **/
    public <T> List<T> queryManyByBlueprint(Blueprint blueprint, Map<String, Object> params) {
        List<T> list = new LinkedList<>();
        this.queryManyByBlueprint(blueprint, params, list);
        return list;
    }

    /**
     * 通过解析好的blueprint, 查询多个, 自传结果容器
     **/
    public  <T> void queryManyByBlueprint(Blueprint blueprint, Map<String, Object> params, Collection<T> resultContainer) {
        this.queryManyByBlueprint(blueprint, new SimpleParamManager(params), resultContainer);
    }

    /**
     * 通过blueprint查询多个，自带结果容器
     **/
    public abstract <T> void queryManyByBlueprint(Blueprint blueprint, ParamManager paramManager, Collection<T> resultContainer);



    /*********************************************** 新增 ***********************************************/

    /**
     * 单个新增
     **/
    public <T> int insertOne(T obj) {
        Class<?> clazz = obj.getClass();
        ModelMeta modelMeta = definer.createOrGetModelMeta(clazz);
        Blueprint blueprint = parser.parseInsertByMeta(modelMeta);
        ParamManager paramManager = paramManagerFactory.getByModelMeta(modelMeta, obj);

        return insertOneByBlueprint(blueprint, paramManager);
    }

    public abstract <T> int insertOneByBlueprint(Blueprint blueprint, ParamManager paramManager);

    /**
     * 批量新增
     **/
    public abstract <T> List<Integer> batchInsert(Collection<T> batchModel, Class<T> dOModelClass);



    /*********************************************** 更新 ***********************************************/

    /**
     * 通过主键id更新
     **/
    public <T> int updateOneById(T obj) {
        Class<?> clazz = obj.getClass();
        ModelMeta modelMeta = definer.createOrGetModelMeta(clazz);
        Blueprint blueprint = parser.parseUpdateByMeta(modelMeta);
        ParamManager paramManager = paramManagerFactory.getByModelMeta(modelMeta, obj);
        return this.updateByBlueprint(blueprint, paramManager);
    }

    /**
     * 通过预解析desc的key, 更新
     **/
    // 通过desc更新
    public int updateByDescKey(String descKey, Map<String, Object> params) {
        Blueprint blueprint = warehouseManager.getByDescKey(descKey);
        return this.updateByBlueprint(blueprint, params);
    }

    /**
     * 通过解析好的Blueprint更新
     **/
    // 通过desc更新
    public int updateByBlueprint(Blueprint blueprint, Map<String, Object> params) {
        SimpleParamManager simpleParamManager = new SimpleParamManager(params);
        return this.updateByBlueprint(blueprint, simpleParamManager);
    }

    /**
     * 通过解析好的Blueprint更新
     **/
    // 通过desc更新
    public abstract int updateByBlueprint(Blueprint blueprint, ParamManager paramManager);

    /**
     * 批量通过主键id更新
     **/
    public abstract <T> int batchUpdateById(Collection<T> obj, Class<T> dOModelClass);



    /*********************************************** 删除 ***********************************************/

    /**
     * 通过id删除单个
     **/
    public <T, I> int deleteById(I id, Class<T> modelClass) {
        ModelMeta modelMeta = definer.createOrGetModelMeta(modelClass);
        Blueprint blueprint = parser.parseDeleteByMeta(modelMeta);
        return this.deleteByBlueprint(blueprint, Collections.singletonMap(Constant.MAIN_ID, id));
    }

    /**
     * 通过id删除多个
     **/
    public <T, I> int deleteByIds(Collection<I> ids, Class<T> modelClass) {
        ModelMeta modelMeta = definer.createOrGetModelMeta(modelClass);
        Blueprint blueprint = parser.parseDeleteByMeta(modelMeta);
        return this.deleteByBlueprint(blueprint, Collections.singletonMap(Constant.MAIN_IDS, ids));
    }

    /**
     * 通过解析好的blueprint删除
     **/
    //
    public abstract int deleteByBlueprint(Blueprint blueprint, Map<String, Object> params);
}
