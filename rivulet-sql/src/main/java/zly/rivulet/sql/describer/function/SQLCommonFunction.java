package zly.rivulet.sql.describer.function;

import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;
import zly.rivulet.base.utils.collector.StatementCollector;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Description 所有普通样式的函数都继承这个  functionName(functionBody1, functionBody2, functionBody3 ... functionBody)
 *
 * @author zhaolaiyuan
 * Date 2022/10/16 13:27
 **/
public  class SQLCommonFunction<F, C> implements SQLFunction<F, C> {

    private final String functionName;

    private final List<SingleValueElementDesc<F, C>> functionBody;

    public SQLCommonFunction(String functionName, List<SingleValueElementDesc<F, C>> functionBody) {
        this.functionName = functionName;
        this.functionBody = functionBody;
    }

    public String getFunctionName() {
        return functionName;
    }

    @Override
    public List<SingleValueElementDesc<?, ?>> getSingleValueList() {
        return (List) this.functionBody;
    }

    @Override
    public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
        return (customCollector, customSingleValueWraps) -> {
            customCollector.append(functionName).append("(");
            customCollector.appendAllSeparator(customSingleValueWraps, ",");
            customCollector.append(")");
        };
    }


}
