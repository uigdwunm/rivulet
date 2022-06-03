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
        return new SQLDescDefineException("位置的查询类型");
    }
}
