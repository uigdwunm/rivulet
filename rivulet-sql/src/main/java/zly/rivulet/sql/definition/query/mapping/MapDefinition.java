package zly.rivulet.sql.definition.query.mapping;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.sql.describer.function.MFunctionDesc;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.SqlParser;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;
import zly.rivulet.sql.parser.node.FieldProxyNode;
import zly.rivulet.sql.parser.node.QueryProxyNode;

public class MapDefinition implements SQLSingleValueElementDefinition {

    private SetMapping<?, ?> selectField;

    private final SingleValueElementDefinition valueDefinition;

    /**
     * 引用别名，就是当前select字段的引用表别名
     **/
    private final SQLAliasManager.AliasFlag referenceAlias;

    /**
     * 别名，这个select字段自己的别名
     **/
    private final SQLAliasManager.AliasFlag aliasFlag;

    public MapDefinition(SingleValueElementDefinition valueDefinition, SQLAliasManager.AliasFlag referenceAlias, SQLAliasManager.AliasFlag aliasFlag) {
        this.valueDefinition = valueDefinition;
        this.referenceAlias = referenceAlias;
        this.aliasFlag = aliasFlag;
    }

    public void setSelectField(SetMapping<?, ?> selectField) {
        this.selectField = selectField;
    }

    public SetMapping<?, ?> getSelectField() {
        return selectField;
    }

    public SingleValueElementDefinition getValueDefinition() {
        return valueDefinition;
    }

    public SQLAliasManager.AliasFlag getAliasFlag() {
        return aliasFlag;
    }

    public SQLAliasManager.AliasFlag getReferenceAlias() {
        return referenceAlias;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }

}
