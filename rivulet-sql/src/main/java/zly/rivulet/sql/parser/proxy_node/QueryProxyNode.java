package zly.rivulet.sql.parser.proxy_node;

import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.parser.SQLAliasManager;

import java.util.List;

public class QueryProxyNode implements SelectNode, FromNode {

    /**
     * 是SqlQueryDefinition, 解析时没啥用,也不能用,提前放到这
     **/
    private final SqlQueryDefinition queryFromMeta;

    private final Object proxyModel;

    /**
     * 当前query的所有from，如果是单表查询则只有一个，但是不会为空
     **/
    private final List<FromNode> fromNodeList;

    /**
     * 当前query的所有select
     **/
    private final List<SelectNode> selectNodeList;
    /**
     * 当前节点的别名flag
     **/
    private final SQLAliasManager.AliasFlag aliasFlag = SQLAliasManager.createAlias();


    protected QueryProxyNode(Object proxyModel, SqlQueryDefinition sqlQueryDefinition, List<FromNode> fromNodeList, List<SelectNode> selectNodeList) {
        this.proxyModel = proxyModel;
        this.queryFromMeta = sqlQueryDefinition;
        this.fromNodeList = fromNodeList;
        this.selectNodeList = selectNodeList;
    }

    @Override
    public QueryFromMeta getQueryFromMeta() {
        return this.queryFromMeta;
    }

    @Override
    public Object getProxyModel() {
        return this.proxyModel;
    }

    public List<FromNode> getFromNodeList() {
        return fromNodeList;
    }

    public List<SelectNode> getSelectNodeList() {
        return selectNodeList;
    }

    @Override
    public SQLAliasManager.AliasFlag getAliasFlag() {
        return this.aliasFlag;
    }

}
