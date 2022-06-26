package zly.rivulet.sql.preparser.helper.node;

/**
 * Description 主要用于获取值
 *
 * @author zhaolaiyuan
 * Date 2022/6/12 14:52
 **/
public class FieldProxyNode implements SelectNode {
    /**
     * 这个字段是否用到了，如果是子查询的情况，有可能多个字段只用到部分
     **/
    private boolean isUsed;
}
