package zly.rivulet.mysql.generator.statement;

import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.generator.SqlStatementFactory;

public class FieldStatement extends SingleValueElementStatement {

    private final FieldMeta fieldMeta;

    private final String referenceAlias;

    public FieldStatement(FieldMeta fieldMeta, String referenceAlias) {
        this.fieldMeta = fieldMeta;
        this.referenceAlias = referenceAlias;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        if (StringUtil.isNotBlank(referenceAlias)) {
            collector.append(referenceAlias).append(Constant.POINT_CHAR).append(fieldMeta.getOriginName());
        } else {
            collector.append(fieldMeta.getOriginName());
        }
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            FieldDefinition.class,
            (definition, soleFlag, initHelper) -> {
                FieldDefinition fieldDefinition = (FieldDefinition) definition;
                // 解析这个查询类型时必须把别名管理器换成相应的，防止多层子查询别名混乱
                SQLAliasManager aliasManager = initHelper.getAliasManager();
                String referenceAlias = aliasManager.getAlias(fieldDefinition.getModelAlias());
                return new FieldStatement(fieldDefinition.getFieldMeta(), referenceAlias);
            },
            (definition, helper) -> {
                FieldDefinition fieldDefinition = (FieldDefinition) definition;
                // 解析这个查询类型时必须把别名管理器换成相应的，防止多层子查询别名混乱
                SQLAliasManager aliasManager = helper.getAliasManager();
                String referenceAlias = aliasManager.getAlias(fieldDefinition.getModelAlias());
                return new FieldStatement(fieldDefinition.getFieldMeta(), referenceAlias);
            }
        );
    }
}
