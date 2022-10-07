package zly.rivulet.sql.parser.proxy_node;

import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.parser.SQLAliasManager;

public class ModelProxyNode implements FromNode {

    /**
     * 当前节点的别名flag
     **/
    private final SQLAliasManager.AliasFlag aliasFlag;

    private final SQLModelMeta queryFromMeta;

    /**
     * 当前对象的代理对象，一定对应一个表对象
     *
     **/
    private final Object proxyModel;

    public ModelProxyNode(SQLAliasManager.AliasFlag aliasFlag, SQLModelMeta queryFromMeta, Object proxyModel) {
        this.aliasFlag = aliasFlag;
        this.queryFromMeta = queryFromMeta;
        this.proxyModel = proxyModel;
    }

    @Override
    public QueryFromMeta getQueryFromMeta() {
        return this.queryFromMeta;
    }

    @Override
    public Object getProxyModel() {
        return this.proxyModel;
    }

    @Override
    public SQLAliasManager.AliasFlag getAliasFlag() {
        return this.aliasFlag;
    }
}
