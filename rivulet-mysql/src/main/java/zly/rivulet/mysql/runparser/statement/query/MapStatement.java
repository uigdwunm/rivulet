package zly.rivulet.mysql.runparser.statement.query;

import zly.rivulet.base.utils.FormatCollector;
import zly.rivulet.base.utils.StatementCollector;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.mysql.runparser.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.runparser.SqlStatementFactory;
import zly.rivulet.sql.runparser.statement.SqlStatement;

public class MapStatement implements SingleValueElementStatement {

    private final SingleValueElementStatement value;

    /**
     * Description 别名
     *
     * @author zhaolaiyuan
     * Date 2022/7/10 11:16
     **/
    private final String alias;

    /**
     * Description 引用别名
     *
     * @author zhaolaiyuan
     * Date 2022/7/10 21:31
     **/
    private final String referenceAlias;

    private static final char POINT = '.';

    private static final String AS = " AS ";

    public MapStatement(SingleValueElementStatement value, String alias, String referenceAlias) {
        this.value = value;
        this.alias = alias;
        this.referenceAlias = referenceAlias;
    }

    @Override
    public void singleCollectStatement(StatementCollector collector) {
        collector.append(referenceAlias).append(POINT).append(this.alias);
    }

    @Override
    public void singleFormatGetStatement(FormatCollector collector) {
        collector.append(referenceAlias).append(POINT).append(this.alias);
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        if (value instanceof MySqlQueryStatement) {
            collector.leftBracket();
            value.singleCollectStatement(collector);
            collector.rightBracket();
            collector.append(AS);
            collector.append(this.alias);
        } else if (StringUtil.isNotBlank(this.referenceAlias)) {
            value.singleCollectStatement(collector);
            collector.append(AS);
            collector.append(this.alias);
        } else {
            value.singleCollectStatement(collector);
        }
    }

    @Override
    public void formatGetStatement(FormatCollector collector) {
        if (value instanceof MySqlQueryStatement) {
            collector.leftBracketLine();
            value.singleFormatGetStatement(collector);
            collector.rightBracketLine();
            collector.append(AS);
            collector.append(this.alias);
        } else if (StringUtil.isNotBlank(this.referenceAlias)) {
            value.singleFormatGetStatement(collector);
            collector.append(AS);
            collector.append(this.alias);
        } else {
            value.singleFormatGetStatement(collector);
        }

    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            MapDefinition.class,
            (definition, soleFlag, initHelper) -> {
                MapDefinition mapDefinition = (MapDefinition) definition;
                SqlStatement value = sqlStatementFactory.init(mapDefinition.getValueDefinition(), soleFlag.subSwitch(), initHelper);
                SQLAliasManager aliasManager = initHelper.getAliasManager();
                String alias = aliasManager.getAlias(mapDefinition.getAliasFlag());
                String referenceAlias = aliasManager.getAlias(mapDefinition.getReferenceAlias());
                return new MapStatement((SingleValueElementStatement) value, alias, referenceAlias);
            },
            (definition, helper) -> {
                MapDefinition mapDefinition = (MapDefinition) definition;
                SqlStatement value = sqlStatementFactory.getOrCreate(mapDefinition.getValueDefinition(), helper);
                SQLAliasManager aliasManager = helper.getAliasManager();
                String alias = aliasManager.getAlias(mapDefinition.getAliasFlag());
                String referenceAlias = aliasManager.getAlias(mapDefinition.getReferenceAlias());
                return new MapStatement((SingleValueElementStatement) value, alias, referenceAlias);
            }
        );
    }
}
