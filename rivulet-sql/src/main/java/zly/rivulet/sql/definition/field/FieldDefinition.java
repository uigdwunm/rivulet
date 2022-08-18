package zly.rivulet.sql.definition.field;

import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.sql.exception.SQLModelDefineException;
import zly.rivulet.sql.parser.SQLAliasManager;

public class FieldDefinition implements SQLSingleValueElementDefinition {

    private final ModelMeta modelMeta;

    private final FieldMeta fieldMeta;

    /**
     * 引用别名，就是当前字段所属范围的引用表别名
     **/
    private final SQLAliasManager.AliasFlag modelAlias;

    public FieldDefinition(SQLAliasManager.AliasFlag modelAlias, ModelMeta modelMeta, FieldMeta fieldMeta) {
        this.modelAlias = modelAlias;
        this.modelMeta = modelMeta;
        this.fieldMeta = fieldMeta;
    }

    public FieldDefinition(SQLAliasManager.AliasFlag modelAlias, SQLModelMeta modelMeta, String fieldName) {
        SQLFieldMeta fieldMeta = modelMeta.getFieldMeta(fieldName);
        if (fieldMeta == null) {
            throw SQLModelDefineException.noField();
        }
        this.modelAlias = modelAlias;
        this.modelMeta = modelMeta;
        this.fieldMeta = fieldMeta;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }

    public SQLAliasManager.AliasFlag getModelAlias() {
        return modelAlias;
    }

    public ModelMeta getModelMeta() {
        return modelMeta;
    }

    public FieldMeta getFieldMeta() {
        return fieldMeta;
    }
}
