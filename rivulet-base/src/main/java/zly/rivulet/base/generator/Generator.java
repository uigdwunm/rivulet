package zly.rivulet.base.generator;

import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.definition.Blueprint;

public interface Generator {

    void warmUp(Blueprint definition);

    /**
     * Description 运行时解析，传入预解析好的definition 和 需要传入的参数，得到真正执行需要的语句
     *
     * @author zhaolaiyuan
     * Date 2021/12/5 12:00
     **/
    Fish generate(Blueprint definition, ParamManager paramManager);
}
