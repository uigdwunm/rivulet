package zly.rivulet.mysql.generator.statement.insert;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definition.insert.ColumnItemDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class ColumnItemStatement implements SqlStatement {

    private final SQLFieldMeta sqlFieldMeta;

    public ColumnItemStatement(SQLFieldMeta sqlFieldMeta) {
        this.sqlFieldMeta = sqlFieldMeta;
    }

    public SQLFieldMeta getSqlFieldMeta() {
        return sqlFieldMeta;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(sqlFieldMeta.getOriginName());
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            ColumnItemDefinition.class,
            (definition, soleFlag, toolbox) -> {
                ColumnItemDefinition columnItemDefinition = (ColumnItemDefinition) definition;
                return new ColumnItemStatement(columnItemDefinition.getSqlFieldMeta());
            },
            (definition, toolbox) -> {
                ColumnItemDefinition columnItemDefinition = (ColumnItemDefinition) definition;
                return new ColumnItemStatement(columnItemDefinition.getSqlFieldMeta());
            }
        );
    }
}
