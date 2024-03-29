package zly.rivulet.sql.parser.proxy_node;

import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.query.SQLQueryDefinition;
import zly.rivulet.sql.describer.query.SQLQueryMetaDesc;
import zly.rivulet.sql.parser.toolbox.SQLParserPortableToolbox;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ProxyNodeManager {

    /**
     * 专用于DOModel智能update保存的proxyNode
     **/
    private final Map<SQLModelMeta, MetaModelProxyNode> metaModelProxyNodeMap = new ConcurrentHashMap<>();

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
    public MetaModelProxyNode getOrCreateProxyMetaModel(SQLModelMeta sqlModelMeta, SQLParserPortableToolbox toolbox) {
        // 每个表的代理对象只能从缓存拿一次
        if (toolbox.repeatProxyNodeCheck(sqlModelMeta)) {
            MetaModelProxyNode metaModelProxyNode = metaModelProxyNodeMap.get(sqlModelMeta);
            if (metaModelProxyNode != null) {
                // 缓存有则可以拿
                return metaModelProxyNode;
            }
        }

        MetaModelProxyNode metaModelProxyNode = new MetaModelProxyNode(sqlModelMeta);
        metaModelProxyNodeMap.put(sqlModelMeta, metaModelProxyNode);
        return metaModelProxyNode;
    }


    public QueryProxyNode createQueryProxyNode(
        SQLQueryDefinition sqlQueryDefinition,
        SQLParserPortableToolbox toolbox,
        SQLQueryMetaDesc<?, ?> metaDesc
    ) {
        RivuletDesc rivuletDesc = metaDesc.getAnnotation();
        if (rivuletDesc == null) {
            return new QueryProxyNode(sqlQueryDefinition, toolbox, metaDesc);
        }
        // 从缓存取
        QueryProxyNode queryProxyNode = new QueryProxyNode(sqlQueryDefinition, toolbox, metaDesc);
        this.queryProxyNodeMap.put(rivuletDesc.value(), queryProxyNode);
        return queryProxyNode;
    }

    public QueryProxyNode createQueryProxyNode(
        SQLParserPortableToolbox toolbox,
        RivuletDesc rivuletDesc,
        Class<?> fromModelClass
    ) {
        if (rivuletDesc == null) {
            return new QueryProxyNode(toolbox, fromModelClass);
        }

        QueryProxyNode queryProxyNode = new QueryProxyNode(toolbox, fromModelClass);
        this.queryProxyNodeMap.put(rivuletDesc.value(), queryProxyNode);
        return queryProxyNode;
    }


    public QueryProxyNode createQueryProxyNode(SQLParserPortableToolbox toolbox, SQLModelMeta sqlModelMeta) {
        return new QueryProxyNode(toolbox, sqlModelMeta);
    }

    public QueryProxyNode getQueryProxyNode(String key) {
        return this.queryProxyNodeMap.get(key);
    }
}
