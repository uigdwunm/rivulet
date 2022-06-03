package zly.rivulet.base.preparser;

import zly.rivulet.base.definition.FinalDefinition;

import java.lang.reflect.Method;

public interface PreParser {

    /**
     * Description 预解析，传入自定义的描述语句，和绑定的方法
     *
     * @author zhaolaiyuan
     * Date 2021/12/5 12:06
     **/
    FinalDefinition parse(String key, Method method);
}
