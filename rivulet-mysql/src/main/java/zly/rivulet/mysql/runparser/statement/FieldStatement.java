package zly.rivulet.mysql.runparser.statement;

import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.utils.FormatCollectHelper;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.runparser.SqlStatementFactory;

public class FieldStatement implements SingleValueElementStatement {

    private final FieldMeta fieldMeta;

    private final String referenceAlias;

    public FieldStatement(FieldMeta fieldMeta, String referenceAlias) {
        this.fieldMeta = fieldMeta;
        this.referenceAlias = referenceAlias;
    }

    @Override
    public String createStatement() {
        if (StringUtil.isBlank(referenceAlias)) {
            return referenceAlias + '.' + fieldMeta.getOriginName();
        } else {
            return fieldMeta.getOriginName();
        }
    }

    @Override
    public void collectStatement(StringBuilder sqlCollector) {
        if (StringUtil.isBlank(referenceAlias)) {
            sqlCollector.append(referenceAlias).append('.').append(fieldMeta.getOriginName());
        } else {
            sqlCollector.append(fieldMeta.getOriginName());
        }
    }

    @Override
    public void formatGetStatement(FormatCollectHelper formatCollectHelper) {
        if (StringUtil.isBlank(referenceAlias)) {
            formatCollectHelper.append(referenceAlias).append('.').append(fieldMeta.getOriginName());
        } else {
            formatCollectHelper.append(fieldMeta.getOriginName());
        }

    }

    public Definition getOriginDefinition() {
        return null;
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            FieldDefinition.class,
            (definition, soleFlag, initHelper) -> {
                FieldDefinition fieldDefinition = (FieldDefinition) definition;
                // 解析这个查询类型时必须把别名管理器换成相应的，防止多层子查询别名混乱
                SQLAliasManager aliasManager = initHelper.getAliasManager();
                String referenceAlias = aliasManager.getAlias(fieldDefinition.getReferenceAlias());
                return new FieldStatement(fieldDefinition.getFieldMeta(), referenceAlias);
            },
            (definition, helper) -> {
                FieldDefinition fieldDefinition = (FieldDefinition) definition;
                // 解析这个查询类型时必须把别名管理器换成相应的，防止多层子查询别名混乱
                SQLAliasManager aliasManager = helper.getAliasManager();
                String referenceAlias = aliasManager.getAlias(fieldDefinition.getReferenceAlias());
                return new FieldStatement(fieldDefinition.getFieldMeta(), referenceAlias);
            }
        );
    }
}
