package zly.rivulet.sql.preparser.helper.node;

import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;

import java.util.List;

public interface FromNode extends ProxyNode {

    Object getProxyModel();

    Class<?> getFromModelClass();

    List<MapDefinition> getMapDefinitionList();

    QueryFromMeta getQueryFromMeta();
}
