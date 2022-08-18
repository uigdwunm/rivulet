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
import zly.rivulet.base.parser.param.ParamDefinitionManager;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.SqlParser;
import zly.rivulet.sql.parser.node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class SetItemDefinition extends AbstractDefinition {

    private final SingleValueElementDefinition valueDefinition;

    private final FieldDefinition fieldDefinition;

    /**
     * 引用别名，就是当前select字段的引用表别名，有可能为空，比如子查询
     **/
    private final SQLAliasManager.AliasFlag referenceAlias;

    public SetItemDefinition(SqlParserPortableToolbox toolbox, Mapping<?, ?, ?> mapping) {
        super(CheckCondition.IS_TRUE, toolbox.getParamDefinitionManager());
        SetMapping<?, ?> mappingField = mapping.getMappingField();
        String setterMethodName = mappingField.parseExecuteMethodName();
        String fieldName = StringUtil.parseSetterMethodNameToFieldName(setterMethodName);
        SqlDefiner sqlDefiner = toolbox.getSqlPreParser().getSqlDefiner();
        new FieldDefinition()

        this.valueDefinition = this.parse(toolbox, mapping.getDesc());
    }


    protected SingleValueElementDefinition parse(SqlParserPortableToolbox toolbox, SingleValueElementDesc<?, ?> singleValueElementDesc) {
        QueryProxyNode currNode = toolbox.getCurrNode();
        if (singleValueElementDesc instanceof FieldMapping) {
            FieldMapping<Object, Object> fieldMapping = (FieldMapping<Object, Object>) singleValueElementDesc;
            return currNode.parseField(fieldMapping);
        } else if (singleValueElementDesc instanceof JoinFieldMapping) {
            JoinFieldMapping<Object> joinFieldMapping = (JoinFieldMapping<Object>) singleValueElementDesc;
            return currNode.parseField(joinFieldMapping);
        } else if (singleValueElementDesc instanceof SqlQueryMetaDesc) {
            SqlParser sqlPreParser = toolbox.getSqlPreParser();
            SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) sqlPreParser.parse((SqlQueryMetaDesc<?, ?>) singleValueElementDesc, toolbox);
            QueryProxyNode subQueryNode = toolbox.getCurrNode();
            currNode.addConditionSubQueryNode(subQueryNode, sqlQueryDefinition);
            // 这里替换回来
            toolbox.setCurrNode(currNode);
            return sqlQueryDefinition;
        } else if (singleValueElementDesc instanceof Param) {
            ParamDefinitionManager paramDefinitionManager = toolbox.getParamDefinitionManager();
            return paramDefinitionManager.registerParam((Param<?>) singleValueElementDesc);
//        } else if (singleValueElementDesc instanceof Function) {
        }
        throw UnbelievableException.unknownType();
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }
}
