package zly.rivulet.mysql.runparser.statement.query;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.utils.FormatCollectHelper;
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

    public MapStatement(SingleValueElementStatement value, String alias, String referenceAlias) {
        this.value = value;
        this.alias = alias;
        this.referenceAlias = referenceAlias;
    }


    @Override
    public String createStatement() {
        if (value instanceof MySqlQueryStatement) {
            return "(" + value.singleCreateStatement() + ") AS " + alias;
        }

        if (StringUtil.isNotBlank(this.referenceAlias)) {
            return value.singleCreateStatement() + " AS " + alias;
        } else {
            return value.singleCreateStatement();
        }
    }

    @Override
    public String singleCreateStatement() {
        return this.referenceAlias + '.' + this.alias;
    }

    @Override
    public void collectStatement(StringBuilder sqlCollector) {

    }

    @Override
    public void formatGetStatement(FormatCollectHelper formatCollectHelper) {

    }

    @Override
    public Definition getOriginDefinition() {
        return null;
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
