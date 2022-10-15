package zly.rivulet.sql.parser.proxy_node;

import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;

public interface FromNode extends ProxyNode {

    ThreadLocal<MapDefinition> THREAD_LOCAL = new ThreadLocal<>();

    QueryFromMeta getQueryFromMeta();

    Object getProxyModel();
}
