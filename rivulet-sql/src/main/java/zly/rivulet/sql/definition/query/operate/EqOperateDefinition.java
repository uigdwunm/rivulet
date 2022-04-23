package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.describer.query.desc.Condition;
import zly.rivulet.sql.preparser.SqlPreParseHelper;

public class EqOperateDefinition extends OperateDefinition {

    private SingleValueElementDefinition leftElement;

    private SingleValueElementDefinition rightElement;

    public EqOperateDefinition(SqlPreParseHelper sqlPreParseHelper, Condition<?, ?, ?> condition) {
        super(condition.get);
        SingleValueElementDesc<?, ?> leftFieldMapped = condition.getLeftFieldMapped();
        SingleValueElementDesc<?, ?> rightFieldMapped = condition.getRightFieldMappeds()[0];

        this.leftElement = sqlPreParseHelper.parse(leftFieldMapped);
        this.rightElement = sqlPreParseHelper.parse(rightFieldMapped);
    }

    public SingleValueElementDefinition getLeftElement() {
        return leftElement;
    }

    public SingleValueElementDefinition getRightElement() {
        return rightElement;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }
}
