package vkllyr.jpaminus.parser.definition.sqlPart;

/**
 * Description 带参数的sql,占位符(?)
 *
 * @author zhaolaiyuan
 * Date 2021/9/25 11:47
 **/
public class PlaceholderSqlPart implements SqlPart {
    private SqlPart sqlPart;

    // 记录自己需要的参数在源方法入参中的索引位置
    private int[] paramIndexes;

    @Override
    public String getSql() {
        return null;
    }

    @Override
    public Object[] getParam(Object[] params) {
        return new Object[0];
    }

    @Override
    public boolean check(Object[] params) {
        return false;
    }
}
