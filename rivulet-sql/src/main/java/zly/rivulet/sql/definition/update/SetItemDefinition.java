package zly.rivulet.sql.definition.update;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.parser.SqlParser;
import zly.rivulet.sql.parser.node.FromNode;
import zly.rivulet.sql.parser.node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class SetItemDefinition extends AbstractDefinition {

    private final FieldDefinition fieldDefinition;

    private final SingleValueElementDefinition valueDefinition;

    public SetItemDefinition(SqlParserPortableToolbox toolbox, FieldDefinition fieldDefinition, Param<?> param) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());

        this.fieldDefinition = fieldDefinition;
        this.valueDefinition = this.parseSingleValue(toolbox, param);
    }

    public SetItemDefinition(SqlParserPortableToolbox toolbox, Mapping<?, ?, ?> mapping) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());

        this.valueDefinition = this.parseSingleValue(toolbox, mapping.getDesc());

        QueryProxyNode currNode = toolbox.getCurrNode();
        // 更新模型仅支持单个 model，所以直接取第一个
        FromNode fromNode = currNode.getFromNodeList().get(0);
        SQLModelMeta modelMeta = this.getModelMeta(toolbox);
        SQLFieldMeta fieldMeta = getFieldName(modelMeta, mapping);
        this.fieldDefinition = new FieldDefinition(fromNode.getAliasFlag(), modelMeta, fieldMeta);
    }

    private SQLModelMeta getModelMeta(SqlParserPortableToolbox toolbox) {
        QueryProxyNode currNode = toolbox.getCurrNode();
        SqlDefiner sqlDefiner = (SqlDefiner) toolbox.getSqlPreParser().getDefiner();
        Class<?> fromModelClass = currNode.getFromModelClass();
        return sqlDefiner.createOrGetModelMeta(fromModelClass);
    }

    private SQLFieldMeta getFieldName(SQLModelMeta modelMeta, Mapping<?, ?, ?> mapping) {
        SetMapping<?, ?> mappingField = mapping.getMappingField();
        String setterMethodName = mappingField.parseExecuteMethodName();
        String fieldName = StringUtil.parseSetterMethodNameToFieldName(setterMethodName);
        return modelMeta.getFieldMeta(fieldName);

    }


    protected SingleValueElementDefinition parseSingleValue(SqlParserPortableToolbox toolbox, SingleValueElementDesc<?, ?> singleValueElementDesc) {
        QueryProxyNode currNode = toolbox.getCurrNode();
        if (singleValueElementDesc instanceof FieldMapping) {
            FieldMapping<Object, Object> fieldMapping = (FieldMapping<Object, Object>) singleValueElementDesc;
            return currNode.parseField(fieldMapping);
        } else if (singleValueElementDesc instanceof JoinFieldMapping) {
            JoinFieldMapping<Object> joinFieldMapping = (JoinFieldMapping<Object>) singleValueElementDesc;
            return currNode.parseField(joinFieldMapping);
        } else if (singleValueElementDesc instanceof SqlQueryMetaDesc) {
            SqlParser sqlPreParser = toolbox.getSqlPreParser();
            SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) sqlPreParser.parseByDesc((SqlQueryMetaDesc<?, ?>) singleValueElementDesc, toolbox);
            QueryProxyNode subQueryNode = toolbox.getCurrNode();
            currNode.addConditionSubQueryNode(subQueryNode, sqlQueryDefinition);
            // 这里替换回来
            toolbox.setCurrNode(currNode);
            return sqlQueryDefinition;
        } else if (singleValueElementDesc instanceof Param) {
            ParamReceiptManager paramReceiptManager = toolbox.getParamReceiptManager();
            return paramReceiptManager.registerParam((Param<?>) singleValueElementDesc);
//        } else if (singleValueElementDesc instanceof Function) {
        }
        throw UnbelievableException.unknownType();
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }

    public SingleValueElementDefinition getValueDefinition() {
        return valueDefinition;
    }

    public FieldDefinition getFieldDefinition() {
        return fieldDefinition;
    }
}
