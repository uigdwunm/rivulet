package vkllyr.jpaminus.parser;

import vkllyr.jpaminus.mapper.definition.MapDefinition;
import vkllyr.jpaminus.parser.definition.SqlDefinition;

public class Parser {

    public static Result parse(SqlDefinition sqlDefinition, Object[] methodParams) {
        String sql = "";
        Object[] sqlParams = null;
        return new Result(sql, sqlParams, sqlDefinition.getMapDefinition());
    }

    public static class Result {
        private final String sql;

        private final Object[] params;

        private final MapDefinition mapDefinition;

        public Result(String sql, Object[] params, MapDefinition mapDefinition) {
            this.sql = sql;
            this.params = params;
            this.mapDefinition = mapDefinition;
        }
    }
}
