package zly.rivulet.mysql.generator.statement.query;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.param.MySQLParamStatement;
import zly.rivulet.sql.definition.query.main.LimitDefinition;
import zly.rivulet.sql.definition.query.main.SkitDefinition;
import zly.rivulet.sql.generator.SQLStatementFactory;
import zly.rivulet.sql.generator.statement.SQLStatement;

public class LimitStatement extends SQLStatement {

    private final MySQLParamStatement limitParam;

    private final MySQLParamStatement skitParam;

    public LimitStatement(MySQLParamStatement limitParam, MySQLParamStatement skitParam) {
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

    public static void registerToFactory(SQLStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            LimitDefinition.class,
            (definition, soleFlag, initHelper) -> {
                LimitDefinition limitDefinition = (LimitDefinition) definition;
                SQLStatement limitParam = sqlStatementFactory.warmUp(limitDefinition.getLimitParam(), soleFlag.subSwitch(), initHelper);
                SkitDefinition skit = limitDefinition.getSkit();
                SQLStatement skitParam = null;
                if (skit != null) {
                    skitParam = sqlStatementFactory.warmUp(skit.getSkit(), soleFlag.subSwitch(), initHelper);
                }
                return new LimitStatement((MySQLParamStatement) limitParam, (MySQLParamStatement) skitParam);
            },
            (definition, helper) -> {
                LimitDefinition limitDefinition = (LimitDefinition) definition;
                SQLStatement limitParam = sqlStatementFactory.getOrCreate(limitDefinition.getLimitParam(), helper);
                SkitDefinition skit = limitDefinition.getSkit();
                SQLStatement skitParam = null;
                if (skit != null) {
                    skitParam = sqlStatementFactory.getOrCreate(skit.getSkit(), helper);
                }
                return new LimitStatement((MySQLParamStatement) limitParam, (MySQLParamStatement) skitParam);
            }
        );
    }
}
