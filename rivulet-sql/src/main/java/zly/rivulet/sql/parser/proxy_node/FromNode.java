package zly.rivulet.sql.parser.proxy_node;

import zly.rivulet.sql.definer.meta.QueryFromMeta;

public interface FromNode extends ProxyNode {

    QueryFromMeta getQueryFromMeta();

    Object getProxyModel();
}
