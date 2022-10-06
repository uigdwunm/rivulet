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

    public FieldDefinition(ModelMeta modelMeta, FieldMeta fieldMeta) {
        this.modelMeta = modelMeta;
        this.fieldMeta = fieldMeta;
    }

    public FieldDefinition(SQLModelMeta modelMeta, String fieldName) {
        SQLFieldMeta fieldMeta = modelMeta.getFieldMetaByFieldName(fieldName);
        if (fieldMeta == null) {
            throw SQLModelDefineException.noField();
        }
        this.modelMeta = modelMeta;
        this.fieldMeta = fieldMeta;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }

    public ModelMeta getModelMeta() {
        return modelMeta;
    }

    public FieldMeta getFieldMeta() {
        return fieldMeta;
    }
}
