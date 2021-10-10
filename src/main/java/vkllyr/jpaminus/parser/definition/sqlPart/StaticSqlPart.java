package vkllyr.jpaminus.parser.definition.sqlPart;

import vkllyr.jpaminus.common.Constant;

public class StaticSqlPart implements SqlPart {
    private final String sql;

    public StaticSqlPart(String sql) {
        this.sql = sql;
    }

    @Override
    public String getSql() {
        return this.sql;
    }

    @Override
    public Object[] getParam(Object[] params) {
        // 返回空数组
        return Constant.EMPTY_OBJECT_ARRAY;
    }

    @Override
    public boolean check(Object[] params) {
        return false;
    }
}
