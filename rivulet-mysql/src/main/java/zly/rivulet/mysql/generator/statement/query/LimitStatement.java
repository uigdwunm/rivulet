package zly.rivulet.mysql.generator.statement.query;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.param.SQLParamStatement;
import zly.rivulet.sql.definition.query.main.LimitDefinition;
import zly.rivulet.sql.definition.query.main.SkitDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class LimitStatement extends SqlStatement {

    private final SQLParamStatement limitParam;

    private final SQLParamStatement skitParam;

    public LimitStatement(SQLParamStatement limitParam, SQLParamStatement skitParam) {
        this.limitParam = limitParam;
        this.skitParam = skitParam;
    }

    @Override
    public int length() {
        if (skitParam != null) {
            return Constant.LIMIT.length() + skitParam.singleLength() + Constant.COMMA.length() + limitParam.singleLength();
        } else {
            return Constant.LIMIT.length() + limitParam.singleLength();
        }
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        if (skitParam != null) {
            collector.append(Constant.LIMIT).append(skitParam).append(Constant.COMMA).append(limitParam);
        } else {
            collector.append(Constant.LIMIT).append(limitParam);
        }
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            LimitDefinition.class,
            (definition, soleFlag, initHelper) -> {
                LimitDefinition limitDefinition = (LimitDefinition) definition;
                SqlStatement limitParam = sqlStatementFactory.warmUp(limitDefinition.getLimitParam(), soleFlag.subSwitch(), initHelper);
                SkitDefinition skit = limitDefinition.getSkit();
                SqlStatement skitParam = null;
                if (skit != null) {
                    skitParam = sqlStatementFactory.warmUp(skit.getSkit(), soleFlag.subSwitch(), initHelper);
                }
                return new LimitStatement((SQLParamStatement) limitParam, (SQLParamStatement) skitParam);
            },
            (definition, helper) -> {
                LimitDefinition limitDefinition = (LimitDefinition) definition;
                SqlStatement limitParam = sqlStatementFactory.getOrCreate(limitDefinition.getLimitParam(), helper);
                SkitDefinition skit = limitDefinition.getSkit();
                SqlStatement skitParam = null;
                if (skit != null) {
                    skitParam = sqlStatementFactory.getOrCreate(skit.getSkit(), helper);
                }
                return new LimitStatement((SQLParamStatement) limitParam, (SQLParamStatement) skitParam);
            }
        );
    }
}
