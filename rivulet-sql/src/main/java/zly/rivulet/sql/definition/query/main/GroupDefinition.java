package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.sql.preparser.SqlPreParseHelper;

import java.util.List;

public class GroupDefinition extends AbstractDefinition {
    protected GroupDefinition() {
        super(CheckCondition.IS_TRUE, null);
    }

    public GroupDefinition(SqlPreParseHelper sqlPreParseHelper, List<? extends FieldMapping<?,?>> groupFieldList) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getSqlParamDefinitionManager());
    }

    @Override
    public GroupDefinition forAnalyze() {
        return null;
    }
}
