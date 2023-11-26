package zly.rivulet.sql.exception;

import java.util.Arrays;

public class SQLDescDefineException extends RuntimeException {

    private SQLDescDefineException(String msg) {
        super(msg);
    }

    /**
     * Description 子查询循环嵌套
     *
     * @author zhaolaiyuan
     * Date 2022/5/21 11:32
     **/
    public static SQLDescDefineException subQueryLoopNesting() {
        return new SQLDescDefineException("子查询循环嵌套");
    }

    public static SQLDescDefineException unknownQueryType(Class<?> type) {
        return new SQLDescDefineException("未知的查询类型, 这个type不是预定义的modelMeta，type=" + type.getName());
    }

    public static SQLDescDefineException selectAndFromNoMatch() {
        return new SQLDescDefineException("select对象和from对象不匹配，无法自动映射查询结果");
    }

    public static SQLDescDefineException unSupportMultiModelUpdate() {
        return new SQLDescDefineException("更新操作仅支持单个模型");
    }

    public static SQLDescDefineException forceAliasRepeat(String ... forceAlias) {
        return new SQLDescDefineException("强制指定的别名重复," + Arrays.toString(forceAlias));
    }

    public static SQLDescDefineException differentForceAlias(String forceAlias) {
        return new SQLDescDefineException("同一个对象存在多个不同的强制别名," + forceAlias);
    }

    public static SQLDescDefineException subQueryMustOriginFrom(String value, Class<?> fieldType, Class<?> fromModel) {
        return new SQLDescDefineException("子查询使用的视图模型必须是原始查询的from模型");
    }

    public static SQLDescDefineException mustQueryKey(String value, Class<?> fieldType) {
        return new SQLDescDefineException("必须使用查询key作为子查询" + value);
    }

    public static SQLDescDefineException noMappedItemList(Class<?> fromModel, Class<?> selectModel) {
        return new SQLDescDefineException("from模型和select模型不一致时，必须定义每个select字段的映射结果,fromMode=" + fromModel.getSimpleName() + ", selectModel=" + selectModel.getSimpleName());
    }
}
