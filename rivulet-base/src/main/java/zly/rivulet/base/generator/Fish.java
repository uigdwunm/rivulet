package zly.rivulet.base.generator;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.statement.Statement;

/**
 * Description Statement汇总的一个对象，会有额外信息，我起不出名字了
 *
 * @author zhaolaiyuan
 * Date 2022/2/1 11:03
 **/
public interface Fish {

    Statement getStatement();

    Blueprint getBlueprint();

    int getLength();
}
