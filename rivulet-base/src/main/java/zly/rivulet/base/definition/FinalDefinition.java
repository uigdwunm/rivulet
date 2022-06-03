package zly.rivulet.base.definition;

import zly.rivulet.base.mapper.MapDefinition;
import zly.rivulet.base.preparser.param.ParamDefinitionManager;

/**
 * Description 总的definition标识
 *
 * @author zhaolaiyuan
 * Date 2022/2/27 10:49
 **/
public interface FinalDefinition extends Definition {
    MapDefinition getMapDefinition();

    ParamDefinitionManager getParamDefinitionManager();
}
