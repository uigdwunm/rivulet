package zly.rivulet.mysql.generator.statement.param;

import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.StaticParam;
import zly.rivulet.base.generator.param_manager.for_proxy_method.CommonParamManager;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.param.SQLParamReceipt;
import zly.rivulet.sql.generator.SqlStatementFactory;

public class SQLParamStatement implements SingleValueElementStatement {

    private final SQLParamReceipt sqlParamDefinition;

    private final String value;

    public SQLParamStatement(SQLParamReceipt sqlParamDefinition, String value) {
        this.sqlParamDefinition = sqlParamDefinition;
        this.value = value;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(value);
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SQLParamReceipt.class,
            (definition, soleFlag, initHelper) -> {
                SQLParamReceipt sqlParamDefinition = (SQLParamReceipt) definition;
                Param<?> originDesc = sqlParamDefinition.getOriginDesc();
                if (originDesc instanceof StaticParam) {
                    StaticParam<?> staticParam = (StaticParam<?>) originDesc;
                    Convertor<Object, ?> convertor = (Convertor<Object, ?>) sqlParamDefinition.getConvertor();
                    String value = convertor.convertToStatement(staticParam.getValue());

                    return new SQLParamStatement(sqlParamDefinition, value);
                } else {
                    // 参数是可变的，标记唯一性不可用
                    soleFlag.invalid();
                    return new SQLParamStatement(sqlParamDefinition, null);
                }
            },
            (definition, helper) -> {
                SQLParamReceipt sqlParamDefinition = (SQLParamReceipt) definition;
                CommonParamManager paramManager = helper.getParamManager();
                String value = paramManager.getStatement(sqlParamDefinition);
                return new SQLParamStatement(sqlParamDefinition, value);
            }
        );
    }
}
