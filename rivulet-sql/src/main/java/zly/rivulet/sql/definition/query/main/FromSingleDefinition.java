package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.preparser.SqlPreParseHelper;

public class FromSingleDefinition extends FromDefinition {

    private QueryFromMeta from;


    public FromSingleDefinition(SqlPreParseHelper sqlPreParseHelper, SqlQueryMetaDesc<?, ?> subQueryFrom) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getSqlParamDefinitionManager());
    }

    public FromSingleDefinition(SqlPreParseHelper sqlPreParseHelper, Class<?> modelFrom) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getSqlParamDefinitionManager());
    }

    @Override
    public FromSingleDefinition forAnalyze() {
        return null;
    }
}
