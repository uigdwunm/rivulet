package zly.rivulet.base.runparser;

import zly.rivulet.base.runparser.statement.Statement;

/**
 * Description Statement汇总的一个对象，会有额外信息，我起不出名字了
 *
 * @author zhaolaiyuan
 * Date 2022/2/1 11:03
 **/
public abstract class Fish {

    private final Statement statement;


    protected Fish(Statement statement) {
        this.statement = statement;
    }

    public Statement getStatement() {
        return statement;
    }
}
