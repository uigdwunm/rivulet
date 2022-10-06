package zly.rivulet.sql.definition.update;

import zly.rivulet.base.definition.AbstractContainerDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.ParamCheckType;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.parser.node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.List;
import java.util.stream.Collectors;

public class SetDefinition extends AbstractContainerDefinition {

    private final View<SetItemDefinition> setItemDefinitionView;

    public SetDefinition(SqlParserPortableToolbox toolbox, List<? extends Mapping<?, ?, ?>> mappedItemList) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        List<SetItemDefinition> list = mappedItemList.stream()
            .map(mapping -> new SetItemDefinition(toolbox, mapping))
            .collect(Collectors.toList());
        this.setItemDefinitionView = View.create(list);
    }

    public SetDefinition(SqlParserPortableToolbox toolbox, SQLModelMeta sqlModelMeta) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        QueryProxyNode currNode = toolbox.getCurrNode();
        List<SetItemDefinition> list = sqlModelMeta.getFieldMetaList().stream()
            .map(fieldMeta -> {
                return new SetItemDefinition(
                    toolbox,
                    new FieldDefinition(currNode.getAliasFlag(), sqlModelMeta, fieldMeta),
                    Param.of(fieldMeta.getFieldType(), fieldMeta.getFieldName(), SqlParamCheckType.NATURE)
                );
            }).collect(Collectors.toList());
        this.setItemDefinitionView = View.create(list);
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }

    public View<SetItemDefinition> getSetItemDefinitionView() {
        return setItemDefinitionView;
    }
}
