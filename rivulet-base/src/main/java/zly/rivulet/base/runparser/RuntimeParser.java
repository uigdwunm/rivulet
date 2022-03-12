package zly.rivulet.base.runparser;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.runparser.param_manager.ParamManager;

public interface RuntimeParser {

    /**
     * Description 运行时解析，传入预解析好的definition 和 需要传入的参数，得到真正执行需要的语句
     *
     * @author zhaolaiyuan
     * Date 2021/12/5 12:00
     **/
    Fish parse(AbstractDefinition definition, ParamManager paramManager);
}
