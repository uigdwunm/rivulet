package zly.rivulet.sql.definition;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.List;
import java.util.function.BiConsumer;

public class SQLCustomDefinition extends AbstractDefinition {

    protected List<SingleValueElementDefinition> singleValueList;

    protected BiConsumer<CustomCollector, List<CustomSingleValueWrap>> customCollect;

    protected SQLCustomDefinition(SqlParserPortableToolbox toolbox) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
    }

    public SQLCustomDefinition(
        List<SingleValueElementDefinition> singleValueList,
        BiConsumer<CustomCollector, List<CustomSingleValueWrap>> customCollect
    ) {
        super(CheckCondition.IS_TRUE, null);
        this.singleValueList = singleValueList;
        this.customCollect = customCollect;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }

    public List<SingleValueElementDefinition> getSingleValueList() {
        return singleValueList;
    }

    public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
        return customCollect;
    }
}
