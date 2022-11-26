package zly.rivulet.sql.definition;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;

import java.util.List;
import java.util.function.BiConsumer;

public class SQLCustomDefinition extends AbstractDefinition {

    protected final List<SingleValueElementDefinition> singleValueList;

    protected final BiConsumer<CustomCollector, List<CustomSingleValueWrap>> customCollect;

    public SQLCustomDefinition(CheckCondition checkCondition, List<SingleValueElementDefinition> singleValueList, BiConsumer<CustomCollector, List<CustomSingleValueWrap>> customCollect) {
        super(checkCondition, null);
        this.singleValueList = singleValueList;
        this.customCollect = customCollect;
    }

    public SQLCustomDefinition(
        List<SingleValueElementDefinition> singleValueList,
        BiConsumer<CustomCollector, List<CustomSingleValueWrap>> customCollect
    ) {
        this(CheckCondition.IS_TRUE, singleValueList, customCollect);
    }

    public List<SingleValueElementDefinition> getSingleValueList() {
        return singleValueList;
    }

    public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
        return customCollect;
    }

    @Override
    public Copier copier() {
        return new Copier(singleValueList, customCollect);
    }

    public class Copier implements Definition.Copier {

        private List<SingleValueElementDefinition> singleValueList;

        private BiConsumer<CustomCollector, List<CustomSingleValueWrap>> customCollect;

        public Copier(List<SingleValueElementDefinition> singleValueList, BiConsumer<CustomCollector, List<CustomSingleValueWrap>> customCollect) {
            this.singleValueList = singleValueList;
            this.customCollect = customCollect;
        }

        public void setSingleValueList(List<SingleValueElementDefinition> singleValueList) {
            this.singleValueList = singleValueList;
        }

        public void setCustomCollect(BiConsumer<CustomCollector, List<CustomSingleValueWrap>> customCollect) {
            this.customCollect = customCollect;
        }

        @Override
        public SQLCustomDefinition copy() {
            return new SQLCustomDefinition(checkCondition, singleValueList, customCollect);
        }
    }
}
