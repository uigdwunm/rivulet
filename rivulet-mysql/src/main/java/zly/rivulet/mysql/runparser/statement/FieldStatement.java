package zly.rivulet.mysql.runparser.statement;

import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.utils.FormatCollector;
import zly.rivulet.base.utils.StatementCollector;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.runparser.SqlStatementFactory;

public class FieldStatement implements SingleValueElementStatement {

    private final FieldMeta fieldMeta;

    private final String referenceAlias;

    private static final char POINT = '.';

    public FieldStatement(FieldMeta fieldMeta, String referenceAlias) {
        this.fieldMeta = fieldMeta;
        this.referenceAlias = referenceAlias;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        if (StringUtil.isNotBlank(referenceAlias)) {
            collector.append(referenceAlias).append(POINT).append(fieldMeta.getOriginName());
        } else {
            collector.append(fieldMeta.getOriginName());
        }
    }

    @Override
    public void formatGetStatement(FormatCollector formatCollector) {
        if (StringUtil.isNotBlank(referenceAlias)) {
            formatCollector.append(referenceAlias).append(POINT).append(fieldMeta.getOriginName());
        } else {
            formatCollector.append(fieldMeta.getOriginName());
        }
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
