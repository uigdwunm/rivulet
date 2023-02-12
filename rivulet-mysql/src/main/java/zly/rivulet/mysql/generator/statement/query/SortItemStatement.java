package zly.rivulet.mysql.generator.statement.query;

import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.orderby.SortItemDefinition;
import zly.rivulet.sql.describer.query.desc.SortItem;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class SortItemStatement extends SqlStatement {

    private final SingleValueElementStatement value;

    private SortItem.SortType sortType;

    public SortItemStatement(SingleValueElementStatement value, SortItem.SortType sortType) {
        this.value = value;
        this.sortType = sortType;
    }
    @Override
    public int length() {
        return value.getLengthOrCache() + 1 + sortType.name().length();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        value.collectStatementOrCache(collector);
        collector.space().append(sortType.name());
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SortItemDefinition.class,
            (definition, soleFlag, initHelper) -> {
                SortItemDefinition sortItemDefinition = (SortItemDefinition) definition;
                SqlStatement value = sqlStatementFactory.warmUp(sortItemDefinition.getSingleValue(), soleFlag.subSwitch(), initHelper);
                return new SortItemStatement((SingleValueElementStatement) value, sortItemDefinition.getSortType());
            },
            (definition, helper) -> {
                SortItemDefinition sortItemDefinition = (SortItemDefinition) definition;
                SqlStatement value = sqlStatementFactory.getOrCreate(sortItemDefinition.getSingleValue(), helper);
                return new SortItemStatement((SingleValueElementStatement) value, sortItemDefinition.getSortType());
            }
        );
    }
}
