package zly.rivulet.sql.parser.proxy_node;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;

public interface SelectNode extends ProxyNode {

    SingleValueElementDefinition getQuerySelectMeta();
}
