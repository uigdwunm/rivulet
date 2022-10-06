package zly.rivulet.sql.parser.proxy_node;

import zly.rivulet.sql.parser.SQLAliasManager;

public interface ProxyNode {

    SQLAliasManager.AliasFlag getAliasFlag();
}
