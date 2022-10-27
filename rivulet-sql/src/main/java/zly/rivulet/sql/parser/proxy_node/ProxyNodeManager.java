package zly.rivulet.sql.parser.proxy_node;

import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.query.SQLBlueprint;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.definition.update.SqlUpdateDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.update.SqlUpdateMetaDesc;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ProxyNodeManager {

    /**
     * 专用于DOModel智能update保存的proxyNode
     **/
    private final Map<SQLModelMeta, MetaModelProxyNode> forUpdateProxyNodeMap = new ConcurrentHashMap<>();

    /**
     * Description 缓存起来，用于解析运行时手动创建的Definition （比如运行是手动指定sort部分definition）
     *
     * @author zhaolaiyuan
     * Date 2022/10/8 19:46
     **/
    private final Map<String, QueryProxyNode> queryProxyNodeMap = new ConcurrentHashMap<>();

    /**
     * Description 获取缓存的modelProxyNode，专门用于ModelUpdate
     *
     * @author zhaolaiyuan
     * Date 2022/10/8 19:42
     **/
    public MetaModelProxyNode getOrCreateProxyMetaModelForModelUpdate(SQLModelMeta sqlModelMeta) {
        MetaModelProxyNode metaModelProxyNode = forUpdateProxyNodeMap.get(sqlModelMeta);
        if (metaModelProxyNode == null) {
            return metaModelProxyNode;
        }

        metaModelProxyNode = new MetaModelProxyNode(sqlModelMeta);
        forUpdateProxyNodeMap.put(sqlModelMeta, metaModelProxyNode);
        return metaModelProxyNode;
    }


    public QueryProxyNode getOrCreateQueryProxyNode(
        SqlQueryDefinition sqlQueryDefinition,
        SqlParserPortableToolbox toolbox,
        SqlQueryMetaDesc<?, ?> metaDesc
    ) {
        QueryProxyNode queryProxyNode;
        RivuletDesc rivuletDesc = metaDesc.getAnnotation();
        if (rivuletDesc == null) {
            queryProxyNode = new QueryProxyNode(sqlQueryDefinition, toolbox, metaDesc);
        } else {
            String key = rivuletDesc.value();
            queryProxyNode = this.queryProxyNodeMap.get(key);
            if (queryProxyNode == null) {
                queryProxyNode = new QueryProxyNode(sqlQueryDefinition, toolbox, metaDesc);
                this.queryProxyNodeMap.put(key, queryProxyNode);
            }
            if (!toolbox.repeatProxyNodeCheck(queryProxyNode)) {
                // 整个语句中会可能会有多个子查询，每个子查询必须要有自己的ProxyNode，要不解析会乱套
                // 所以从缓存拿ProxyNode没问题，但是只能拿一次，如果出现重复的，一定得新建
                queryProxyNode = new QueryProxyNode(sqlQueryDefinition, toolbox, metaDesc);
            }
        }
        return queryProxyNode;
    }

    public QueryProxyNode getOrCreateQueryProxyNode(
        SqlUpdateDefinition sqlUpdateDefinition,
        SqlParserPortableToolbox toolbox,
        SqlUpdateMetaDesc<?> metaDesc
    ) {
        QueryProxyNode queryProxyNode;
        if (metaDesc.getAnnotation() == null) {
            queryProxyNode = new QueryProxyNode(toolbox, metaDesc.getMainFrom());
        } else {
            queryProxyNode = this.queryProxyNodeMap.get(metaDesc.getAnnotation().value());
        }
        if (queryProxyNode == null) {
            queryProxyNode = new QueryProxyNode(toolbox, metaDesc.getMainFrom());
            this.queryProxyNodeMap.put(, queryProxyNode);
        }
        // update不用检查重复，因为update一定是最外层才有的
        return queryProxyNode;

    }

    public QueryProxyNode getQueryProxyNode(SqlQueryDefinition sqlQueryDefinition) {
        return this.queryProxyNodeMap.get(sqlQueryDefinition);
    }
}
