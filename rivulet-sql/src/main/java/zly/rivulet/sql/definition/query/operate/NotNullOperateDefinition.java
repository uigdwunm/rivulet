package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.describer.query.desc.AbstractCondition;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

public class NotNullOperateDefinition extends OperateDefinition {

    private SQLSingleValueElementDefinition elementDesc;

    public NotNullOperateDefinition(SqlPreParseHelper sqlPreParseHelper, AbstractCondition<?, ?, ?> condition) {
        super(condition.getCheckCondition(), sqlPreParseHelper.getSqlParamDefinitionManager());
        SingleValueElementDesc<?, ?> elementDesc = condition.getLeftFieldMapped();

        this.elementDesc = sqlPreParseHelper.parse(elementDesc);
    }

    public SQLSingleValueElementDefinition getElementDesc() {
        return elementDesc;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }
}
