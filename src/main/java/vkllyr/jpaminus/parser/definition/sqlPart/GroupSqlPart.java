package vkllyr.jpaminus.parser.definition.sqlPart;

public class GroupSqlPart implements SqlPart {

    @Override
    public String[] getSqlAndArgs(Object[] params) {
        return new String[0];
    }

    @Override
    public boolean check(Object[] params) {
        return false;
    }
}
