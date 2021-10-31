package vkllyr.jpaminus.parser.definition;

import vkllyr.jpaminus.mapper.definition.MapDefinition;
import vkllyr.jpaminus.parser.definition.sqlPart.SqlPart;

public class SqlDefinition {
    private final SqlPart[] sqlPartArray;

    private final MapDefinition mapDefinition;

    public SqlDefinition(SqlPart[] sqlPartArray, MapDefinition mapDefinition) {
        this.sqlPartArray = sqlPartArray;
        this.mapDefinition = mapDefinition;
    }

    public MapDefinition getMapDefinition() {
        return mapDefinition;
    }

}
