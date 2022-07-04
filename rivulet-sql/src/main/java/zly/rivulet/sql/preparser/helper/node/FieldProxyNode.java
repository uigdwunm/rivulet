package zly.rivulet.sql.preparser.helper.node;

import zly.rivulet.sql.preparser.SQLAliasManager;

/**
 * Description 主要用于获取值
 *
 * @author zhaolaiyuan
 * Date 2022/6/12 14:52
 **/
public class FieldProxyNode implements SelectNode {

    private final QueryProxyNode parentNode;

    /**
     * 这个字段是否用到了，如果是子查询的情况，有可能多个字段只用到部分
     **/
    private boolean isUsed;

    /**
     * 当前节点的别名flag
     **/
    private final SQLAliasManager.AliasFlag aliasFlag;

    public FieldProxyNode(QueryProxyNode parentNode, SQLAliasManager.AliasFlag aliasFlag) {
        this.parentNode = parentNode;
        this.aliasFlag = aliasFlag;
    }

    @Override
    public SQLAliasManager.AliasFlag getAliasFlag() {
        return aliasFlag;
    }

}
