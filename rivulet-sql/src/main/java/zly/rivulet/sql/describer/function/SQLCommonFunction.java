package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;

import java.util.List;

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

    public List<SingleValueElementDesc<F, C>> getFunctionBody() {
        return functionBody;
    }

    public String getFunctionName() {
        return functionName;
    }
}
