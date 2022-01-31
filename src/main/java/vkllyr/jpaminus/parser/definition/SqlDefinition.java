package vkllyr.jpaminus.parser.definition;

import vkllyr.jpaminus.mapper.definition.MapDefinition;
import vkllyr.jpaminus.parser.definition.sqlPart.SqlPart;
import vkllyr.jpaminus.utils.MPair;
import vkllyr.jpaminus.utils.MStringBuilder;

import java.util.LinkedList;
import java.util.List;

public class SqlDefinition {
    private final SqlPart[] sqlPartArray;

    // 映射器定义
    private final MapDefinition mapDefinition;

    public SqlDefinition(SqlPart[] sqlPartArray, MapDefinition mapDefinition) {
        this.sqlPartArray = sqlPartArray;
        this.mapDefinition = mapDefinition;
    }

    public MapDefinition getMapDefinition() {
        return mapDefinition;
    }

    public MPair<String, Object[]> getSqlAndParams(Object[] args) {
        MStringBuilder mStringBuilder = new MStringBuilder();
        List<Object> params = new LinkedList<>();

        // TODO

        return MPair.of(mStringBuilder.toString(), params.toArray());
    }

}
