package zly.rivulet.sql.preparser.helper.node;

import zly.rivulet.sql.definition.query.mapping.MappingDefinition;

import java.util.List;
import java.util.Objects;

public interface FromNode extends ProxyNode {

    Object getProxyModel();

    Class<?> getFromModelClass();

    List<MappingDefinition> getMappingDefinitionList();
}
