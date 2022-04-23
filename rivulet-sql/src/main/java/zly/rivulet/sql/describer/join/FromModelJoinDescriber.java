package zly.rivulet.sql.describer.join;

public class FromModelJoinDescriber<F> extends JoinDescriber<F> {

    /**
     * Description 主表的model，也可以是子查询
     *
     * @author zhaolaiyuan
     * Date 2022/4/4 15:22
     **/
    private final Class<F> modelFrom;

    public FromModelJoinDescriber(Class<F> modelFrom) {
        this.modelFrom = modelFrom;
    }
}
