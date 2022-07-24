package zly.rivulet.mysql.runparser.statement.param;

import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.StandardParam;
import zly.rivulet.base.describer.param.StaticParam;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.preparser.param.ParamDefinitionManager;
import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.base.utils.FormatCollector;
import zly.rivulet.mysql.runparser.statement.SingleValueElementStatement;
import zly.rivulet.sql.definition.param.SQLParamDefinition;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.runparser.SqlStatementFactory;

public class SQLParamStatement implements SingleValueElementStatement {

    private final SQLParamDefinition sqlParamDefinition;

    private final String value;

    public SQLParamStatement(SQLParamDefinition sqlParamDefinition, String value) {
        this.sqlParamDefinition = sqlParamDefinition;
        this.value = value;
    }

    @Override
    public String createStatement() {
        return value;
    }

    @Override
    public void collectStatement(StringBuilder sqlCollector) {

    }

    @Override
    public void formatGetStatement(FormatCollector formatCollector) {
        Param<?> originDesc = sqlParamDefinition.getOriginDesc();
        if (originDesc instanceof StandardParam) {
            StandardParam<?> standardParam = (StandardParam<?>) originDesc;
            SqlParamCheckType sqlParamCheckType = sqlParamDefinition.getSqlParamCheckType();
            switch (sqlParamCheckType) {
                case NATURE:
                    formatCollector.append("#{");
                    break;
                case PLACEHOLDER:
                    formatCollector.append("${");
                    break;
            }
            formatCollector.append(standardParam.getPathKey()).append('}');
        } else if (originDesc instanceof StaticParam) {
            // 写死的参数
            formatCollector.append(this.value);
        } else {
            throw UnbelievableException.unknownType();
        }
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SQLParamDefinition.class,
            (definition, soleFlag, initHelper) -> {
                SQLParamDefinition sqlParamDefinition = (SQLParamDefinition) definition;
                Param<?> originDesc = sqlParamDefinition.getOriginDesc();
                if (originDesc instanceof StaticParam) {
                    ParamDefinitionManager paramDefinitionManager = initHelper.getParamDefinitionManager();
                    String value = paramDefinitionManager.getStaticStatement(sqlParamDefinition);
                    return new SQLParamStatement(sqlParamDefinition, value);
                } else {
                    // 参数是可变的，标记唯一性不可用
                    soleFlag.invalid();
                    return new SQLParamStatement(sqlParamDefinition, null);
                }
            },
            (definition, helper) -> {
                SQLParamDefinition sqlParamDefinition = (SQLParamDefinition) definition;
                ParamManager paramManager = helper.getParamManager();
                String value = paramManager.getStatement(sqlParamDefinition);
                return new SQLParamStatement(sqlParamDefinition, value);
            }
        );
    }
}
