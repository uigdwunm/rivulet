package zly.rivulet.sql.definition.query.mapping;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.definition.param.ParamDefinition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.SelectMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.describer.function.MFunctionDesc;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.preparser.SqlParamDefinitionManager;
import zly.rivulet.sql.preparser.SqlPreParser;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;
import zly.rivulet.sql.preparser.helper.node.FieldProxyNode;
import zly.rivulet.sql.preparser.helper.node.QueryProxyNode;

import java.lang.reflect.Method;

public class MappingDefinition implements SingleValueElementDefinition {

    private SingleValueElementDesc<?, ?> desc;

    private SelectMapping<?, ?> selectField;

    private SingleValueElementDefinition definition;

    /**
     * 引用别名，就是当前select字段的引用表别名，有可能为空，比如子查询
     **/
    private SQLAliasManager.AliasFlag referenceAlias;

    /**
     * 别名，这个select字段自己的别名
     **/
    private SQLAliasManager.AliasFlag aliasFlag;

    public MappingDefinition(FieldDefinition fieldDefinition, SQLAliasManager.AliasFlag referenceAlias, SQLAliasManager.AliasFlag aliasFlag) {
        this.definition = fieldDefinition;
        this.referenceAlias = referenceAlias;
        this.aliasFlag = aliasFlag;
    }

    public MappingDefinition(SqlPreParseHelper sqlPreParseHelper, Mapping.Item<?, ?, ?> item) {
        SingleValueElementDesc<?, ?> desc = item.getDesc();
        this.desc = desc;
        this.selectField = item.getSelectField();
        QueryProxyNode currNode = sqlPreParseHelper.getCurrNode();

        if (desc instanceof FieldMapping) {
            FieldMapping<Object, Object> fieldMapping = (FieldMapping<Object, Object>) desc;
            SingleValueElementDefinition definition = currNode.parseField(fieldMapping);
            if (definition instanceof FieldDefinition) {
                FieldDefinition fieldDefinition = (FieldDefinition) definition;
                this.referenceAlias = fieldDefinition.getReferenceAlias();
                SQLAliasManager.AliasFlag aliasFlag = SQLAliasManager.createFieldAlias(fieldDefinition.getFieldMeta().getOriginName());
                currNode.addSelectNode(new FieldProxyNode(currNode, aliasFlag));
                this.aliasFlag = aliasFlag;
            } else if (definition instanceof MappingDefinition) {
                // 如果是vo对象的get映射方法，则会返回MappingDefinition
                MappingDefinition mappingDefinition = (MappingDefinition) definition;
                this.referenceAlias = mappingDefinition.referenceAlias;
                this.aliasFlag = mappingDefinition.aliasFlag;
            } else {
                throw UnbelievableException.unknownType();
            }
            this.definition = definition;
        } else if (desc instanceof SqlQueryMetaDesc) {
            SqlPreParser sqlPreParser = sqlPreParseHelper.getSqlPreParser();
            Method method = sqlPreParseHelper.getMethod();
            FinalDefinition finalDefinition = sqlPreParser.parse((SqlQueryMetaDesc<?, ?>) desc, method);
            QueryProxyNode subQueryNode = sqlPreParseHelper.getCurrNode();
            currNode.addSelectNode(subQueryNode);
            // 这里替换回来
            sqlPreParseHelper.setCurrNode(currNode);
            this.definition = (SingleValueElementDefinition) finalDefinition;
        } else if (desc instanceof Param) {
            SqlParamDefinitionManager sqlParamDefinitionManager = sqlPreParseHelper.getSqlParamDefinitionManager();
            ParamDefinition paramDefinition = sqlParamDefinitionManager.register((Param<?>) desc);
            this.definition = paramDefinition;
        } else if (desc instanceof MFunctionDesc) {

        } else {
            throw UnbelievableException.unknownType();
        }

    }

    public SelectMapping<?, ?> getSelectField() {
        return selectField;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }

}
