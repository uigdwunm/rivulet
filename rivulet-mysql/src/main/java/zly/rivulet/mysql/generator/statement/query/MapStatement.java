package zly.rivulet.mysql.generator.statement.query;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;
import zly.rivulet.sql.parser.SQLAliasManager;

public class MapStatement extends SQLStatement implements SingleValueElementStatement {

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
    public int length() {
        if (StringUtil.isNotBlank(this.referenceAlias)) {
            return this.referenceAlias.length() + 1 + value.singleLength();
        } else {
            return value.singleLength();
        }
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        if (StringUtil.isNotBlank(this.referenceAlias)) {
            collector.append(this.referenceAlias).append(Constant.POINT_CHAR);
        }
        value.singleCollectStatement(collector);
    }

    public void selectItemCollectStatement(StatementCollector collector) {
        collectStatement(collector);

        if (StringUtil.isNotBlank(this.alias)) {
            collector.space().append(Constant.AS);
            collector.append(this.alias);
        }

    }

    protected int selectItemLength() {
        if (StringUtil.isNotBlank(this.alias)) {
            return this.length() + 1 + Constant.AS.length() + this.alias.length();
        } else {
            return this.length();
        }
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            MapDefinition.class,
            (definition, soleFlag, initHelper) -> {
                MapDefinition mapDefinition = (MapDefinition) definition;
                SQLStatement value = sqlStatementFactory.warmUp(mapDefinition.getValueDefinition(), soleFlag.subSwitch(), initHelper);
                SQLAliasManager aliasManager = initHelper.getAliasManager();
                String alias = aliasManager.getAlias(mapDefinition.getAliasFlag());
                String referenceAlias = aliasManager.getAlias(mapDefinition.getReferenceAlias());
                return new MapStatement((SingleValueElementStatement) value, alias, referenceAlias);
            },
            (definition, helper) -> {
                MapDefinition mapDefinition = (MapDefinition) definition;
                SQLStatement value = sqlStatementFactory.getOrCreate(mapDefinition.getValueDefinition(), helper);
                SQLAliasManager aliasManager = helper.getAliasManager();
                String alias = aliasManager.getAlias(mapDefinition.getAliasFlag());
                String referenceAlias = aliasManager.getAlias(mapDefinition.getReferenceAlias());
                return new MapStatement((SingleValueElementStatement) value, alias, referenceAlias);
            }
        );
    }
}
