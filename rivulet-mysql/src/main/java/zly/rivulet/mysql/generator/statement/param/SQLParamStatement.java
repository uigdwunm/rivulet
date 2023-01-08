package zly.rivulet.mysql.generator.statement.param;

import zly.rivulet.base.definition.param.StaticParamReceipt;
import zly.rivulet.base.describer.param.ParamCheckType;
import zly.rivulet.base.describer.param.StandardParam;
import zly.rivulet.base.generator.param_manager.for_proxy_method.CommonParamManager;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.param.SQLParamReceipt;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.utils.collector.SQLStatementCollector;

public class SQLParamStatement extends SingleValueElementStatement {

    private final String value;

    private final ParamCheckType sqlParamCheckType;

    public SQLParamStatement(String value, ParamCheckType sqlParamCheckType) {
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

    @Override
    public int singleValueLength() {
        return this.length();
    }


    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SQLParamReceipt.class,
            (definition, soleFlag, initHelper) -> {
                SQLParamReceipt sqlParamDefinition = (SQLParamReceipt) definition;
                StandardParam<?> originDesc = sqlParamDefinition.getOriginDesc();
                // 参数是可变的，标记唯一性不可用
                soleFlag.invalid();
                return new SQLParamStatement(null, sqlParamDefinition.getSqlParamCheckType());
            },
            (definition, helper) -> {
                SQLParamReceipt sqlParamDefinition = (SQLParamReceipt) definition;
                CommonParamManager paramManager = helper.getParamManager();
                String value = paramManager.getStatement(sqlParamDefinition);
                return new SQLParamStatement(value, sqlParamDefinition.getSqlParamCheckType());
            }
        );

        sqlStatementFactory.register(
            StaticParamReceipt.class,
            (definition, soleFlag, initHelper) -> {
                StaticParamReceipt staticParamReceipt = (StaticParamReceipt) definition;
                Object paramValue = staticParamReceipt.getParamValue();
                String value = staticParamReceipt.getConvertor().convert(paramValue);
                return new SQLParamStatement(value, ParamCheckType.NATURE);
            },
            (definition, helper) -> {
                SQLParamReceipt sqlParamDefinition = (SQLParamReceipt) definition;
                CommonParamManager paramManager = helper.getParamManager();
                String value = paramManager.getStatement(sqlParamDefinition);
                return new SQLParamStatement(value, sqlParamDefinition.getSqlParamCheckType());
            }
        );
    }
}
