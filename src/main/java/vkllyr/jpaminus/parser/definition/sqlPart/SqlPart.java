package vkllyr.jpaminus.parser.definition.sqlPart;

public interface SqlPart {

    String getSql();

    Object[] getParam(Object[] params);

    boolean check(Object[] params);
}
