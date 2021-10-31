package vkllyr.jpaminus.parser.definition.sqlPart;

/**
 * Description 静态的sql部分，不随查询参数的改变而改变
 *
 * @author zhaolaiyuan
 * Date 2021/10/10 11:31
 **/
public class StaticSqlPart implements SqlPart {
    private final String sql;

    public StaticSqlPart(String sql) {
        this.sql = sql;
    }

    @Override
    public String[] getSql(Object[] params) {
        return new String[]{this.sql};
    }

    @Override
    public boolean check(Object[] params) {
        return true;
    }
}
