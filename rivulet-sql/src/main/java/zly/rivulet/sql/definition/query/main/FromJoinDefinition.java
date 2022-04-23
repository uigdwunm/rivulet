package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.preparser.SqlParamDefinitionManager;
import zly.rivulet.sql.preparser.SqlPreParseHelper;
import zly.rivulet.sql.preparser.SqlPreParser;

public class FromJoinDefinition extends FromDefinition {

    public FromJoinDefinition(SqlPreParseHelper sqlPreParseHelper, Class<?> joinModel) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getSqlParamDefinitionManager());
    }

    @Override
    public FromJoinDefinition forAnalyze() {
        return null;
    }
}
