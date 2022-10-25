package zly.rivulet.sql.definition.update;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.parser.proxy_node.MetaModelProxyNode;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class SetItemDefinition extends AbstractDefinition {

    private final MapDefinition fieldMap;

    private final SingleValueElementDefinition valueDefinition;

    public SetItemDefinition(SqlParserPortableToolbox toolbox, MapDefinition fieldMap, Param<?> param) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());

        QueryProxyNode currNode = toolbox.getQueryProxyNode();
        this.valueDefinition = toolbox.parseSingValueForSelect(currNode.getProxyModel(), param);
        this.fieldMap = fieldMap;
    }

    public SetItemDefinition(SqlParserPortableToolbox toolbox, Mapping<?, ?, ?> mapping) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        QueryProxyNode currNode = toolbox.getQueryProxyNode();
        this.valueDefinition = toolbox.parseSingValueForSelect(currNode.getProxyModel(), mapping.getDesc());
        // 更新模型仅支持单个 model，所以直接取第一个
        MetaModelProxyNode metaModelProxyNode = (MetaModelProxyNode) currNode.getFromNodeList().get(0);
        SQLModelMeta modelMeta = metaModelProxyNode.getQueryFromMeta();
        SQLFieldMeta fieldMeta = getFieldName(modelMeta, mapping);
        this.fieldMap = new MapDefinition(fieldMeta, metaModelProxyNode.getAliasFlag(), null);
    }

    private SQLFieldMeta getFieldName(SQLModelMeta modelMeta, Mapping<?, ?, ?> mapping) {
        SetMapping<?, ?> mappingField = mapping.getMappingField();
        String setterMethodName = mappingField.parseExecuteMethodName();
        String fieldName = StringUtil.parseSetterMethodNameToFieldName(setterMethodName);
        return modelMeta.getFieldMetaByFieldName(fieldName);
    }



    @Override
    public Definition forAnalyze() {
        return null;
    }

    public SingleValueElementDefinition getValueDefinition() {
        return valueDefinition;
    }
}
