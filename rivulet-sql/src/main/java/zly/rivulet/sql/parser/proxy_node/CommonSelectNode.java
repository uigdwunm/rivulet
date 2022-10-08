package zly.rivulet.sql.parser.proxy_node;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.sql.parser.SQLAliasManager;

public class CommonSelectNode implements SelectNode {
    /**
     * 当前节点的别名flag
     **/
    private final SQLAliasManager.AliasFlag aliasFlag;

    private final SingleValueElementDefinition singleValueElementDefinition;

    public CommonSelectNode(SQLAliasManager.AliasFlag aliasFlag, SingleValueElementDefinition singleValueElementDefinition) {
        this.aliasFlag = aliasFlag;
        this.singleValueElementDefinition = singleValueElementDefinition;
    }

    @Override
    public SQLAliasManager.AliasFlag getAliasFlag() {
        return this.aliasFlag;
    }

    @Override
    public SingleValueElementDefinition getQuerySelectMeta() {
        return this.singleValueElementDefinition;
    }
}
