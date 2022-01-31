package pers.zly.sql.definition.query;

import pers.zly.base.definition.AbstractDefinition;
import pers.zly.base.definition.Definition;

import java.util.List;

public class SqlQueryDefinition implements Definition {

    private List<AbstractDefinition> subDefinitionList;

    public List<AbstractDefinition> getSubDefinitionList() {
        return subDefinitionList;
    }
}
