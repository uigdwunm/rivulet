package zly.rivulet.mysql.generator.statement.query;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.param.SQLParamStatement;
import zly.rivulet.sql.definition.query.main.LimitDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class LimitStatement extends SqlStatement {

    private final SQLParamStatement limitParam;

    public LimitStatement(SQLParamStatement limitParam) {
        this.limitParam = limitParam;
    }

    @Override
    protected int length() {
        return limitParam.getLengthOrCache();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(Constant.LIMIT).append(limitParam);
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            LimitDefinition.class,
            (definition, soleFlag, initHelper) -> {
                LimitDefinition limitDefinition = (LimitDefinition) definition;
                SqlStatement limitParam = sqlStatementFactory.warmUp(limitDefinition.getLimitParam(), soleFlag.subSwitch(), initHelper);
                return new LimitStatement((SQLParamStatement) limitParam);
            },
            (definition, helper) -> {
                LimitDefinition limitDefinition = (LimitDefinition) definition;
                SqlStatement limitParam = sqlStatementFactory.getOrCreate(limitDefinition.getLimitParam(), helper);
                return new LimitStatement((SQLParamStatement) limitParam);
            }
        );
    }
}
