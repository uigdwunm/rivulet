package zly.rivulet.sql.definition.query;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.preparser.param.ParamDefinitionManager;

/**
 * Description 标识一个未完成的Definition
 * 用于解决循环嵌套子查询
 *
 * @author zhaolaiyuan
 * Date 2022/5/21 10:56
 **/
public class HalfFinalDefinition implements FinalDefinition {

    public static final HalfFinalDefinition instance = new HalfFinalDefinition();

    @Override
    public Definition forAnalyze() {
        return null;
    }

    @Override
    public Assigner<?> getAssigner() {
        return null;
    }

    @Override
    public ParamDefinitionManager getParamDefinitionManager() {
        return null;
    }

    private HalfFinalDefinition() {}
}
