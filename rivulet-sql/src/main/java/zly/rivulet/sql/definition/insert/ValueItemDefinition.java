package zly.rivulet.sql.definition.insert;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class ValueItemDefinition extends AbstractDefinition {
    private final SQLFieldMeta sqlFieldMeta;

    protected ValueItemDefinition(SqlParserPortableToolbox toolbox, SQLFieldMeta fieldMeta) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        toolbox.getParamReceiptManager().registerParam(
            Param.of(
                fieldMeta.getFieldType(),
                fieldMeta.getFieldName(),
                SqlParamCheckType.NATURE
            )
        );
        this.sqlFieldMeta = fieldMeta;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }
}
