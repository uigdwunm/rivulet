package zly.rivulet.sql.definition.query.operate;

import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.condition.ConditionElement;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class EqOperateDefinition extends OperateDefinition {

    private SingleValueElementDefinition leftElement;

    private SingleValueElementDefinition rightElement;

    public EqOperateDefinition(SqlParserPortableToolbox sqlPreParseHelper, Condition<?, ?> condition) {
        this(sqlPreParseHelper, (ConditionElement<?, ?>) condition);
    }

    public EqOperateDefinition(SqlParserPortableToolbox sqlPreParseHelper, ConditionElement<?, ?> condition) {
        super(condition.getCheckCondition(), sqlPreParseHelper.getParamReceiptManager());
        SingleValueElementDesc<?, ?> leftFieldMapped = condition.getLeftFieldMapped();
        SingleValueElementDesc<?, ?> rightFieldMapped = condition.getRightFieldMappeds()[0];

        this.leftElement = super.parse(sqlPreParseHelper, leftFieldMapped);
        this.rightElement = super.parse(sqlPreParseHelper, rightFieldMapped);
    }

    public EqOperateDefinition(SqlParserPortableToolbox sqlPreParseHelper, MapDefinition mapDefinition, Param<?> param, CheckCondition checkCondition) {
        super(checkCondition, sqlPreParseHelper.getParamReceiptManager());

        this.leftElement =  mapDefinition;
        this.rightElement = super.parse(sqlPreParseHelper, param);
    }

    public SingleValueElementDefinition getLeftElement() {
        return leftElement;
    }

    public SingleValueElementDefinition getRightElement() {
        return rightElement;
    }

    @Override
    public EqOperateDefinition forAnalyze() {
        return null;
    }
}
