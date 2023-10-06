package zly.rivulet.sql.parser.proxy_node;

import zly.rivulet.sql.definition.query_.mapping.MapDefinition;
import zly.rivulet.sql.parser.SQLAliasManager;

public class CommonSelectNode implements SelectNode {
    /**
     * 当前节点的别名flag
     **/
    private final SQLAliasManager.AliasFlag aliasFlag;

    private final MapDefinition mapDefinition;

    public CommonSelectNode(SQLAliasManager.AliasFlag aliasFlag, MapDefinition mapDefinition) {
        this.aliasFlag = aliasFlag;
        this.mapDefinition = mapDefinition;
    }

    @Override
    public SQLAliasManager.AliasFlag getAliasFlag() {
        return this.aliasFlag;
    }

    @Override
    public MapDefinition getQuerySelectMeta() {
        return this.mapDefinition;
    }
}
