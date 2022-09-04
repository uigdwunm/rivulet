package zly.rivulet.base.parser;

import zly.rivulet.base.definer.Definer;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.describer.WholeDesc;

public interface Parser {

    /**
     * Description 预解析，传入自定义的描述语句，和绑定的方法
     * @author zhaolaiyuan
     * Date 2021/12/5 12:06
     **/
    Blueprint parseByKey(String key);

    Blueprint parseByDesc(WholeDesc wholeDesc);

    Blueprint parseInsertByMeta(ModelMeta modelMeta);

    Blueprint parseUpdateByMeta(ModelMeta modelMeta);

    Blueprint parseDeleteByMeta(ModelMeta modelMeta);

    Blueprint parseSelectByMeta(ModelMeta modelMeta);

    Definer getDefiner();
}
