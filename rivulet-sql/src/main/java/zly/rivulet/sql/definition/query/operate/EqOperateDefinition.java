package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.describer.query.desc.AbstractCondition;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

public class EqOperateDefinition extends OperateDefinition {

    private SingleValueElementDefinition leftElement;

    private SingleValueElementDefinition rightElement;

    public EqOperateDefinition(SqlPreParseHelper sqlPreParseHelper, AbstractCondition<?, ?, ?> condition) {
        super(condition.getCheckCondition(), sqlPreParseHelper.getSqlParamDefinitionManager());
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
