package zly.rivulet.sql.definition.update;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.List;

public class SetDefinition extends AbstractDefinition {

//    private final View<SetItemDefinition> setItemDefinitionView;

    public SetDefinition(SqlParserPortableToolbox toolbox, List<? extends Mapping<?, ?, ?>> mappedItemList) {

    }

    @Override
    public Definition forAnalyze() {
        return null;
    }
}
