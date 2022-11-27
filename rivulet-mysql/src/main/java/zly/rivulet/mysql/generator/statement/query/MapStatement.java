package zly.rivulet.mysql.generator.statement.query;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;
import zly.rivulet.sql.parser.SQLAliasManager;

public class MapStatement extends SingleValueElementStatement {

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

    public MapStatement(SingleValueElementStatement value, String alias, String referenceAlias) {
        this.value = value;
        this.alias = alias;
        this.referenceAlias = referenceAlias;
    }

    @Override
    public void singleCollectStatement(StatementCollector collector) {
        collector.append(referenceAlias).append(Constant.POINT_CHAR).append(this.alias);
    }

    @Override
    public int singleValueLength() {
        return referenceAlias.length() + 1 + this.alias.length();
    }

    @Override
    protected int length() {
        int length = 0;
        if (value instanceof MySqlQueryStatement) {
            length += 1;
            length += value.singleValueLength();
            length += 1;
            length += 1 + Constant.AS.length() + 1;
            length += this.alias.length();
        } else if (StringUtil.isNotBlank(this.referenceAlias)) {
            length += value.singleValueLength();
            length += 1 + Constant.AS.length();
            length += this.alias.length();
        } else {
            length += value.singleValueLength();
        }
        return length;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        if (value instanceof MySqlQueryStatement) {
            collector.leftBracket();
            value.singleCollectStatement(collector);
            collector.rightBracket();
        } else if (StringUtil.isNotBlank(this.referenceAlias)) {
            value.singleCollectStatement(collector);
        } else {
            value.singleCollectStatement(collector);
        }

        if (StringUtil.isNotBlank(this.alias)) {
            collector.space().append(Constant.AS);
            collector.append(this.alias);

        }
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            MapDefinition.class,
            (definition, soleFlag, initHelper) -> {
                MapDefinition mapDefinition = (MapDefinition) definition;
                SqlStatement value = sqlStatementFactory.warmUp(mapDefinition.getValueDefinition(), soleFlag.subSwitch(), initHelper);
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
