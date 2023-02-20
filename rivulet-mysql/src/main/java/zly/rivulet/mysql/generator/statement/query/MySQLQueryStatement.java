package zly.rivulet.mysql.generator.statement.query;

import zly.rivulet.base.generator.param_manager.for_proxy_method.CommonParamManager;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.definition.query.SQLQueryDefinition;
import zly.rivulet.sql.definition.query.main.SkitDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;

import java.util.List;
import java.util.stream.Collectors;

public class MySQLQueryStatement extends SQLStatement implements QueryFromStatement {

    private final SQLQueryDefinition definition;

    /**
     * 子语句列表，select、from、where之类的
     * 有序
     **/
    private final List<SQLStatement> subStatementList;

    private MySQLQueryStatement(SQLQueryDefinition definition, List<SQLStatement> subStatementList) {
        this.definition = definition;
        this.subStatementList = subStatementList;
    }

    @Override
    public int length() {
        int length = 0;
        length += this.subStatementList.size() - 1;
        length += this.subStatementList.stream().map(SQLStatement::getLengthOrCache).reduce(0, Integer::sum);
        return length;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        for (SQLStatement subStatement : collector.createJoiner(Constant.SPACE, this.subStatementList)) {
            subStatement.collectStatement(collector);
        }
    }

    @Override
    public int singleLength() {
        return this.length() + 2;
    }

    @Override
    public void singleCollectStatement(StatementCollector collector) {
        collector.leftBracket();
        this.collectStatement(collector);
        collector.rightBracket();
    }

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SQLQueryDefinition.class,
            (definition, soleFlag, initHelper) -> {
                SQLQueryDefinition sqlQueryDefinition = (SQLQueryDefinition) definition;
                List<SQLStatement> subStatementList = sqlQueryDefinition.getSubDefinitionList().stream()
                    // 要跳过skit节点，要不mysql整不了
                    .filter(subDefinition -> !(subDefinition instanceof SkitDefinition))
                    .map(subDefinition -> sqlStatementFactory.warmUp(subDefinition, soleFlag.subSwitch(), initHelper))
                    .collect(Collectors.toList());
                return new MySQLQueryStatement(sqlQueryDefinition, subStatementList);
            },
            (definition, helper) -> {
                SQLQueryDefinition sqlQueryDefinition = (SQLQueryDefinition) definition;
                CommonParamManager paramManager = helper.getParamManager();
                List<SQLStatement> subStatementList = sqlQueryDefinition.getSubDefinitionList().stream()
                    // 要跳过skit节点，要不mysql整不了
                    .filter(subDefinition -> !(subDefinition instanceof SkitDefinition))
                    .filter(subDefinition -> subDefinition.check(paramManager))
                    .map(subDefinition -> sqlStatementFactory.getOrCreate(subDefinition, helper))
                    .collect(Collectors.toList());

                return new MySQLQueryStatement(sqlQueryDefinition, subStatementList);
            }
        );
    }
}
