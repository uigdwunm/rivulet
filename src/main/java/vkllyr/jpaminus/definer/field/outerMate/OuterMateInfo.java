package vkllyr.jpaminus.definer.field.outerMate;

public abstract class OuterMateInfo {

    // 字段名称，必填
    private final String name;

    // 注释，非必填
    private final String desc;

    protected OuterMateInfo(String name, String desc) {
       this.name = name;
       this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * Description 检查参数是否合格，因为是定义阶段，如果有问题直接抛出异常，所以不必有返回值。
     *
     * @author zhaolaiyuan
     * Date 2021/10/30 10:33
     **/
    public abstract void check();

    /**
     * Description 转化成语句
     *
     * @author zhaolaiyuan
     * Date 2021/10/30 10:50
     **/
    public abstract String convertToStatement();
}
