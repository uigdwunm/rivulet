package zly.rivulet.sql.parser.proxy_node;

import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class QueryProxyNode implements SelectNode, FromNode {

    /**
     * 父级节点（生成时传入，可能没有）
     **/
    private final ProxyNode parentNode;

    /**
     * 当前节点的别名flag（生成时传入，可能没有）
     **/
    private final SQLAliasManager.AliasFlag aliasFlag;

    /**
     * Description 查询一定有一个模型对象，可能是复杂查询对象QueryComplexModel,可能是表对象
     *
     * @author zhaolaiyuan
     * Date 2022/10/6 10:38
     **/
    private final Class<?> fromModelClass;

    /**
     * 是SqlQueryDefinition,解析过程中就会放到这里
     **/
    private final QueryFromMeta queryFromMeta;

    private final SQLAliasManager sqlAliasManager;

//    /**
//     * 代理模型统一管理，如果生成了新的代理模型也要存进去（包括用代理model填充的vo对象）
//     **/
//    private final ProxyModelManager proxyModelManager;

    /**
     * Description 新建一个最外层的模型，没有父级节点的那种
     *
     * @author zhaolaiyuan
     * Date 2022/10/6 10:46
     **/
    public QueryProxyNode(SqlQueryDefinition sqlQueryDefinition, SqlParserPortableToolbox toolbox, Class<?> mainFrom) {
        this(null, null, sqlQueryDefinition, toolbox, mainFrom);
    }

    private QueryProxyNode(ProxyNode parentNode, SQLAliasManager.AliasFlag aliasFlag, SqlQueryDefinition sqlQueryDefinition ,SqlParserPortableToolbox toolbox, Class<?> mainFrom) {
        this.parentNode = parentNode;
        this.aliasFlag = aliasFlag;
        this.fromModelClass = mainFrom;
        this.queryFromMeta = sqlQueryDefinition;
        this.sqlAliasManager = sqlQueryDefinition.getAliasManager();
    }

    public QueryProxyNode createSubQueryProxyNode(SqlQueryDefinition sqlQueryDefinition, SqlParserPortableToolbox toolbox, Class<?> mainFrom) {
        SQLAliasManager parentAliasManager = toolbox.getQueryProxyNode().getSqlAliasManager();
        return new QueryProxyNode(this, parentAliasManager.createAlias(), sqlQueryDefinition, toolbox, mainFrom);
    }

    @Override
    public QueryFromMeta getQueryFromMeta() {
        return this.queryFromMeta;
    }

    @Override
    public SQLAliasManager.AliasFlag getAliasFlag() {
        return this.aliasFlag;
    }

    public SQLAliasManager getSqlAliasManager() {
        return sqlAliasManager;
    }
}
