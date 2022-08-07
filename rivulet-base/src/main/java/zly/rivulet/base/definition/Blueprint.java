package zly.rivulet.base.definition;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.preparser.param.ParamDefinitionManager;

/**
 * Description 完全信息的Definition，设计图
 *
 * @author zhaolaiyuan
 * Date 2022/2/27 10:49
 **/
public interface Blueprint extends Definition {
    Assigner<?> getAssigner();

    ParamDefinitionManager getParamDefinitionManager();
}
