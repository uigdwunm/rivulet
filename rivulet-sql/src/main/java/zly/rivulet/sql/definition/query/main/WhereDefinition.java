package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractContainerDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definition.query.operate.OperateDefinition;
import zly.rivulet.sql.describer.query.desc.Condition;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

import java.util.ArrayList;
import java.util.List;

public class WhereDefinition extends AbstractContainerDefinition {

    private final ArrayList<OperateDefinition> operateDefinitionList = new ArrayList<>();

    private WhereDefinition(CheckCondition checkCondition) {
        super(checkCondition, null);
    }

    public WhereDefinition(SqlPreParseHelper sqlPreParseHelper, List<? extends Condition<?, ?>> whereItemList) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getSqlParamDefinitionManager());

        List<OperateDefinition> operateDefinitionList = this.operateDefinitionList;
        for (Condition<?, ?> item : whereItemList) {
            operateDefinitionList.add(item.getOperate().createDefinition(sqlPreParseHelper, item));
        }
    }

    @Override
    public WhereDefinition forAnalyze() {
        WhereDefinition whereDefinition = new WhereDefinition(super.getCheckCondition());
        return null;
    }

}
