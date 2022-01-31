package pers.zly.base.preparser;

import pers.zly.base.definition.Definition;
import pers.zly.base.describer.Desc;

import java.lang.reflect.Method;

public interface PreParser {

    /**
     * Description 预解析，传入自定义的描述语句，和绑定的方法
     *
     * @author zhaolaiyuan
     * Date 2021/12/5 12:06
     **/
    Definition parse(Desc desc, Method method);
}
