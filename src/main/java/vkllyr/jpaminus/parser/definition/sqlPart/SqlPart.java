package vkllyr.jpaminus.parser.definition.sqlPart;

import vkllyr.jpaminus.utils.MStringBuilder;

import java.util.List;

public interface SqlPart {

    void buildSqlAndArgs(MStringBuilder sqlBuilder, List<Object> paramsBuilder, Object[] originParams);

    boolean check(Object[] originParams);

    default int getSort(Object[] originParams) {
        return Integer.MAX_VALUE;
    };
}
