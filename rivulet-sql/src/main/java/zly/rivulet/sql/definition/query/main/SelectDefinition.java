package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.discriber.query.desc.Mapping;

import java.util.List;

public class SelectDefinition extends AbstractDefinition {

    protected SelectDefinition() {
        super(CheckCondition.IS_TRUE);
    }

    public SelectDefinition(Class<?> selectModel, List<? extends Mapping.Item<?, ?, ?>> mappedItemList, boolean nameMapped) {
        this();
    }

    @Override
    public SelectDefinition clone() {
        return null;
    }
}