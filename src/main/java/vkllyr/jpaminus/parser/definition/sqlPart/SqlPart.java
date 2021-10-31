package vkllyr.jpaminus.parser.definition.sqlPart;

public interface SqlPart {

    String[] getSqlAndArgs(Object[] params);

    boolean check(Object[] params);
}
