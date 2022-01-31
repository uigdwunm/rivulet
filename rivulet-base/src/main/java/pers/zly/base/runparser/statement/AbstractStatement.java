package pers.zly.base.runparser.statement;

import pers.zly.base.definition.Definition;
import pers.zly.base.runparser.StatementFactory;
import pers.zly.base.utils.RelationSwitch;

public abstract class AbstractStatement implements Statement {

    /**
     * Description 是否可以字符串语句的
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 12:43
     **/
    private RelationSwitch cacheSwitch;

    protected String cache;

    /**
     * Description 所有子类需要实现的构造方法，子类也不能加别的参数，因为工厂方法要用，不对外暴露
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 11:03
     **/
    protected AbstractStatement(
        Definition definition,
        RelationSwitch cacheSwitch,
        StatementFactory statementFactory
    ) {
        // 这里空实现，每个继承类自己都要有这个构造方法，工厂方法调用
        // 主要流程是，
        // 1, 先校验是否需要存在这个statement节点，
        // 2, 再从缓存或工厂方法创建得到statement类，
        // 3, 最后按固定接口拼接到statement树上
    }


    public abstract Definition getOriginDefinition();

    public boolean canCacheStr() {
        return cacheSwitch.isEnable();
    }
}
