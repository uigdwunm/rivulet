package zly.rivulet.sql.definition.update;

import zly.rivulet.base.utils.View;

public class SetDefinition {

    private final View<SetItemDefinition> setItemDefinitionView;

    public SetDefinition(View<SetItemDefinition> setItemDefinitionView) {
        this.setItemDefinitionView = setItemDefinitionView;
    }
}
