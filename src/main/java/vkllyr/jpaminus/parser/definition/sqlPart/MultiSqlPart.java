package vkllyr.jpaminus.parser.definition.sqlPart;


import vkllyr.jpaminus.describer.query.definition.conditions.CheckCondition;
import vkllyr.jpaminus.utils.MStringBuilder;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MultiSqlPart implements SqlPart {

    private final List<SqlPart> subSqlParts;

    private final CheckCondition<?> checkCondition;

    // 是否需要排序
    private final boolean isNeedSort;

    public MultiSqlPart(CheckCondition<?> checkCondition, Parameter[] parameters) {
        this(checkCondition, parameters, false);
    }

    public MultiSqlPart(CheckCondition<?> checkCondition, Parameter[] parameters, boolean isNeedSort) {
        this.subSqlParts = new ArrayList<>();
        checkCondition.init(parameters);
        this.checkCondition = checkCondition;
        this.isNeedSort = isNeedSort;
    }

    public void add(SqlPart sqlPart) {
        subSqlParts.add(sqlPart);
    }

    @Override
    public boolean check(Object[] originParams) {
        return checkCondition.checkCondition(originParams);
    }

    @Override
    public void buildSqlAndArgs(MStringBuilder sqlBuilder, List<Object> paramsBuilder, Object[] originParams) {
        List<SqlPart> subSqlParts = this.subSqlParts;
        if (isNeedSort) {
            subSqlParts = this.subSqlParts.stream()
                .sorted(Comparator.comparing(sqlPart -> sqlPart.getSort(originParams)))
                .collect(Collectors.toList());
        }

        for (SqlPart subSqlPart : subSqlParts) {
            if (subSqlPart.check(originParams)) {
                subSqlPart.buildSqlAndArgs(sqlBuilder, paramsBuilder, originParams);
            }
        }
    }
}
