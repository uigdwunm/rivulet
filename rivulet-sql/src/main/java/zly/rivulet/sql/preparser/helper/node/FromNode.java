package zly.rivulet.sql.preparser.helper.node;

import java.util.Objects;

public interface FromNode {

    Object getProxyModel();

    Class<?> getModelClass();

    QueryProxyNode getParentNode();
}
