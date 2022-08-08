package zly.rivulet.base.parser;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.describer.WholeDesc;

public interface PreParser {

    /**
     * Description 预解析，传入自定义的描述语句，和绑定的方法
     * @author zhaolaiyuan
     * Date 2021/12/5 12:06
     **/
    Blueprint parse(String key);

    Blueprint parse(WholeDesc wholeDesc);
}
