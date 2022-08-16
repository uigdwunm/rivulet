package zly.rivulet.sql.definition.update;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.parser.SQLAliasManager;

public class SetItemDefinition extends AbstractDefinition {

    private final SingleValueElementDefinition valueDefinition;

    private final FieldDefinition fieldDefinition;

    /**
     * 引用别名，就是当前select字段的引用表别名，有可能为空，比如子查询
     **/
    private final SQLAliasManager.AliasFlag referenceAlias;

    public SetItemDefinition(SingleValueElementDefinition valueDefinition, FieldDefinition fieldDefinition, SQLAliasManager.AliasFlag referenceAlias) {
        super();
        this.valueDefinition = valueDefinition;
        this.fieldDefinition = fieldDefinition;
        this.referenceAlias = referenceAlias;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }
}
