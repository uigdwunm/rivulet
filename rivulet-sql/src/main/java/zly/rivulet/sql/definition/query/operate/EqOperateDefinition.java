package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.describer.query.desc.AbstractCondition;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

public class EqOperateDefinition extends OperateDefinition {

    private SQLSingleValueElementDefinition leftElement;

    private SQLSingleValueElementDefinition rightElement;

    public EqOperateDefinition(SqlPreParseHelper sqlPreParseHelper, AbstractCondition<?, ?, ?> condition) {
        super(condition.getCheckCondition(), sqlPreParseHelper.getSqlParamDefinitionManager());
        SingleValueElementDesc<?, ?> leftFieldMapped = condition.getLeftFieldMapped();
        SingleValueElementDesc<?, ?> rightFieldMapped = condition.getRightFieldMappeds()[0];

        this.leftElement = sqlPreParseHelper.parse(leftFieldMapped);
        this.rightElement = sqlPreParseHelper.parse(rightFieldMapped);
    }

    public SQLSingleValueElementDefinition getLeftElement() {
        return leftElement;
    }

    public SQLSingleValueElementDefinition getRightElement() {
        return rightElement;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }
}
