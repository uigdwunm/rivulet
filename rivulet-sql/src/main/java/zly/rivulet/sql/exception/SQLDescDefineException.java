package zly.rivulet.sql.exception;

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

    public static SQLDescDefineException subQueryNotQuery() {
        return new SQLDescDefineException("嵌套的不是查询语句");
    }

    public static SQLDescDefineException unknowQueryType() {
        return new SQLDescDefineException("未知的查询类型");
    }

    public static SQLDescDefineException selectAndFromNoMatch() {
        return new SQLDescDefineException("select对象和from对象不匹配，无法自动映射查询结果");
    }

    public static SQLDescDefineException unSupportMultiModelUpdate() {
        return new SQLDescDefineException("更新操作仅支持单个模型");
    }

    public static SQLDescDefineException forceAliasRepeat(String forceAlias) {
        return new SQLDescDefineException("强制指定的别名重复," + forceAlias);
    }

    public static SQLDescDefineException subQueryMustOriginSelect(String value, Class<?> fieldType, Class<?> selectModel) {
        return new SQLDescDefineException("子查询使用的视图模型必须是原始查询的select模型");
    }
}
