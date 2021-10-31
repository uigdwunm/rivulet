package vkllyr.jpaminus.parser.definition.sqlPart;

import vkllyr.jpaminus.utils.ArrayUtils;
import vkllyr.jpaminus.describer.query.desc.Param;

import java.util.Arrays;

/**
 * Description 根据判断条件的sql
 *
 * @author zhaolaiyuan
 * Date 2021/9/25 11:47
 **/
public class ConditionSqlPart implements SqlPart, InitParameter {
    // sql语句，可能要拼参数上去，所以是数组
    private final String[] sql;
    // 需要被拼接到sql的参数在上面数组的索引
    private final int[] sqlIndexes;
    // 需要被添加到
    private final int[] paramIndexes;

    // 需要的参数
    private final Param<?>[] params;
    // 记录自己需要的参数在源方法入参中的索引位置
    private int[] originParamIndexes;

    private CheckCondition<?> checkCondition;

    public ConditionSqlPart(String[] sql, int[] sqlIndexes, Param<?>[] params) {
        this.sql = sql;
        this.sqlIndexes = sqlIndexes;
        this.params = params;
    }

    @Override
    public String[] getSql(Object[] originParams) {
        Object[] args = ArrayUtils.fill(originParams, originParamIndexes);

        return null;
    }

    private String[] join(Object[] args) {
        int length = sql.length;
        String[] copy = Arrays.copyOf(sql, length);
        int ai = 0;
        int si = 0;
        for (int i = 0; i < length; i++) {
            if (i == sqlIndexes[si]) {
                Object arg = args[ai];
                if (arg instanceof Character || arg instanceof String) {
                    copy[i] = "'" + arg + "'";
                }
            }
        }
    }

    @Override
    public boolean check(Object[] originParams) {
        return checkCondition.checkExist(originParams);
    }

    @Override
    public Param<?>[] getParams() {
        return params;
    }

    @Override
    public void setParamIndexes(int[] paramIndexes) {
        this.originParamIndexes = paramIndexes;
    }
}
