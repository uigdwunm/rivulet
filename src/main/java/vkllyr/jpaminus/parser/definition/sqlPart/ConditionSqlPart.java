package vkllyr.jpaminus.parser.definition.sqlPart;

import vkllyr.jpaminus.describer.Param;
import vkllyr.jpaminus.describer.query.definition.conditions.CheckCondition;
import vkllyr.jpaminus.parser.definition.ParamDefinition;
import vkllyr.jpaminus.utils.ArrayUtils;
import vkllyr.jpaminus.utils.MStringBuilder;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

/**
 * Description 根据判断条件的sql
 *
 * @author zhaolaiyuan
 * Date 2021/9/25 11:47
 **/
public class ConditionSqlPart implements SqlPart {
    // sql语句，可能要拼参数上去，所以是数组
    private final String[] sql;
    // 需要被拼接到sql的参数在上面数组的索引
    private final int[] sqlIndexes;
    // 分两部分，前半部分按上面顺序的参数定义属于需要拼到sql中的，后半部分是占位符类型的
    private final ParamDefinition[] paramDefinitions;

    // 检测条件，如果检测不通过则舍弃掉这部分sqlPart
    private final CheckCondition<?> checkCondition;

    // 排序，并非所有sqlPart都需要排序，
    private final ParamDefinition sort;

    public ConditionSqlPart(String[] sql, int[] sqlIndexes, Param[] params, Parameter[] parameters) {
        this(sql, sqlIndexes, params, CheckCondition.IS_TRUE, Param.staticOf(Integer.MAX_VALUE), parameters);
    }

    public ConditionSqlPart(String[] sql, int[] sqlIndexes, Param[] params, CheckCondition<?> checkCondition, Parameter[] parameters) {
        this(sql, sqlIndexes, params, checkCondition, Param.staticOf(Integer.MAX_VALUE), parameters);
    }

    public ConditionSqlPart(String[] sql, int[] sqlIndexes, Param[] params, CheckCondition<?> checkCondition, Param.StaticParam<Integer> sort, Parameter[] parameters) {
        this.sql = sql;
        this.sqlIndexes = sqlIndexes;

        this.sort = ParamDefinition.of(sort);

        int length = params.length;
        this.paramDefinitions = new ParamDefinition[length];
        for (int i = 0; i < length; i++) {
            paramDefinitions[i] = ParamDefinition.of(params[i], parameters);
        }

        checkCondition.init(parameters);
        this.checkCondition = checkCondition;
    }

    @Override
    public int getSort(Object[] originParams) {
        return (int) this.sort.getParam(originParams);
    }

    @Override
    public boolean check(Object[] originParams) {
        return checkCondition.checkCondition(originParams);
    }

    @Override
    public void buildSqlAndArgs(MStringBuilder sqlBuilder, List<Object> paramsBuilder, Object[] originParams) {
        int allParamsLength = ArrayUtils.length(paramDefinitions);
        int joinedParamsLength = ArrayUtils.length(sqlIndexes);

        String[] copySqls;
        if (joinedParamsLength == 0) {
            // 没有需要拼接到sql的，则无需复制
            copySqls = this.sql;
        } else {
            // 有参数需要拼接到sql，则复制一份再拼接，否则修改原数组会有并发问题
            copySqls = Arrays.copyOf(this.sql, this.sql.length);
        }

        for (int i = 0; i < allParamsLength; i++) {
            if (i < joinedParamsLength) {
                // 这部分是需要拼接的参数
                copySqls[sqlIndexes[i]] = paramDefinitions[i].getStatment(originParams);
            } else {
                // 这部分是占位符类型的参数
                paramsBuilder.add(paramDefinitions[i].getStatment(originParams));
            }
        }

        sqlBuilder.append(copySqls);
    }
}
