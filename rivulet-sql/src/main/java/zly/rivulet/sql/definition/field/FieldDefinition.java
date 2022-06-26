package zly.rivulet.sql.definition.field;

import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;

public class FieldDefinition implements SingleValueElementDefinition {

    /**
     * 引用别名，就是当前字段所属范围的引用表别名
     **/
    private final SQLAliasManager.AliasFlag referenceAlias;

    /**
     * 当前字段本身的别名，不一定有
     **/
    private final SQLAliasManager.AliasFlag aliasFlag;

    private final ModelMeta modelMeta;

    private final FieldMeta fieldMeta;

    public FieldDefinition(SQLAliasManager.AliasFlag referenceAlias, SQLAliasManager.AliasFlag aliasFlag, ModelMeta modelMeta, FieldMeta fieldMeta) {
        this.referenceAlias = referenceAlias;
        this.aliasFlag = aliasFlag;
        this.modelMeta = modelMeta;
        this.fieldMeta = fieldMeta;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }
}
