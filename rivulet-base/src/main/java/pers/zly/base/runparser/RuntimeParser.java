package pers.zly.base.runparser;

import pers.zly.base.definition.AbstractDefinition;
import pers.zly.base.definition.Definition;
import pers.zly.base.runparser.statement.Statement;

public interface RuntimeParser {

    /**
     * Description 运行时解析，传入预解析好的definition 和 需要传入的参数，得到真正执行需要的语句
     *
     * @author zhaolaiyuan
     * Date 2021/12/5 12:00
     **/
    Statement parse(AbstractDefinition definition, Object[] args);
}
