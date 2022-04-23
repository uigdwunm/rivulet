package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.preparser.ParamDefinitionManager;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.preparser.SqlPreParseHelper;

public abstract class FromDefinition extends AbstractDefinition {

    protected FromDefinition(CheckCondition checkCondition, ParamDefinitionManager paramDefinitionManager) {
        super(checkCondition, paramDefinitionManager);
    }

    public static FromDefinition createFromDefinition(SqlPreParseHelper sqlPreParseHelper, Class<?> modelFrom, SqlQueryMetaDesc<?, ?> subQueryFrom) {
        if (subQueryFrom != null) {
            return new FromSingleDefinition(sqlPreParseHelper, subQueryFrom);
        } else if ()
        return null;
    }

    @Override
    public FromDefinition forAnalyze() {
        return null;
    }
}
