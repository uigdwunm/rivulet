package zly.rivulet.sql.parser.proxy_node;

import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.SqlParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ProxyNodeManager {
    private static final ThreadLocal<MapDefinition> THREAD_LOCAL = new ThreadLocal<>();

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
//    private final Map<SqlQueryDefinition, QueryProxyNode> queryProxyNodeMap = new ConcurrentHashMap<>();

    private final SqlParser sqlParser;

    private final SqlDefiner sqlDefiner;

    public ProxyNodeManager(SqlParser sqlParser) {
        this.sqlParser = sqlParser;
        this.sqlDefiner = sqlParser.getDefiner();
    }


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


    private Object createProxyComplexModelFrom(Class<?> fromModelClass, SQLAliasManager.AliasFlag aliasFlag) {
        return ClassUtils.dynamicProxy(
            fromModelClass,
            (method, args) -> {
                MapDefinition mapDefinition = THREAD_LOCAL.get();
                if (mapDefinition != null) {
                    THREAD_LOCAL.set(new MapDefinition(mapDefinition, aliasFlag, SQLAliasManager.createAlias()));
                }
            }
        );
    }

    public MapDefinition getFieldDefinitionFromThreadLocal(FieldMapping<?, ?> fieldMapping, Object proxyModel) {
        ((FieldMapping<Object, Object>) fieldMapping).getMapping(proxyModel);
        return THREAD_LOCAL.get();
    }
}
