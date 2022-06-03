package zly.rivulet.sql.definition.field;

import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;

public class FieldDefinition implements SingleValueElementDefinition {

    /**
     * 所属表的别名标识
     **/
    private final SQLAliasManager.AliasFlag aliasFlag;

    private final ModelMeta modelMeta;

    private final FieldMeta fieldMeta;

    public FieldDefinition(SQLAliasManager.AliasFlag aliasFlag, ModelMeta modelMeta, FieldMeta fieldMeta) {
        this.aliasFlag = aliasFlag;
        this.modelMeta = modelMeta;
        this.fieldMeta = fieldMeta;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }
}
