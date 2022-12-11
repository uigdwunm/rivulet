package zly.rivulet.mysql.generator.statement.param;

import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.StaticParam;
import zly.rivulet.base.generator.param_manager.for_proxy_method.CommonParamManager;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.param.SQLParamReceipt;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.utils.collector.SQLStatementCollector;

public class SQLParamStatement extends SingleValueElementStatement {

    private final String value;

    private final SqlParamCheckType sqlParamCheckType;

    public SQLParamStatement(String value, SqlParamCheckType sqlParamCheckType) {
        this.value = value;
        this.sqlParamCheckType = sqlParamCheckType;
    }

    @Override
    protected int length() {
        return value.length();
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        switch (sqlParamCheckType) {
            case PLACEHOLDER:
                collector.append(Constant.QUESTION_MARK);
                SQLStatementCollector sqlStatementCollector = (SQLStatementCollector) collector;
                sqlStatementCollector.collectPlaceholderParam(value);
                return;
            case NATURE:
                collector.append(value);
                return;
        }
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SQLParamReceipt.class,
            (definition, soleFlag, initHelper) -> {
                SQLParamReceipt sqlParamDefinition = (SQLParamReceipt) definition;
                Param<?> originDesc = sqlParamDefinition.getOriginDesc();
                if (originDesc instanceof StaticParam) {
                    StaticParam<?> staticParam = (StaticParam<?>) originDesc;
                    String value = sqlParamDefinition.getConvertor().convert(staticParam.getValue());

                    return new SQLParamStatement(value, sqlParamDefinition.getSqlParamCheckType());
                } else {
                    // 参数是可变的，标记唯一性不可用
                    soleFlag.invalid();
                    return new SQLParamStatement(null, sqlParamDefinition.getSqlParamCheckType());
                }
            },
            (definition, helper) -> {
                SQLParamReceipt sqlParamDefinition = (SQLParamReceipt) definition;
                CommonParamManager paramManager = helper.getParamManager();
                String value = paramManager.getStatement(sqlParamDefinition);
                return new SQLParamStatement(value, sqlParamDefinition.getSqlParamCheckType());
            }
        );
    }

    @Override
    public int singleValueLength() {
        return this.length();
    }
}
