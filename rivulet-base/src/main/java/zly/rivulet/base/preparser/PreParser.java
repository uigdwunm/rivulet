package zly.rivulet.base.preparser;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.describer.Desc;
import zly.rivulet.base.describer.WholeDesc;

import java.lang.reflect.Method;
import java.util.function.BiFunction;

public interface PreParser {

    /**
     * Description 预解析，传入自定义的描述语句，和绑定的方法
     *
     * @author zhaolaiyuan
     * Date 2021/12/5 12:06
     **/
    FinalDefinition parse(WholeDesc wholeDesc, Method method);
}
