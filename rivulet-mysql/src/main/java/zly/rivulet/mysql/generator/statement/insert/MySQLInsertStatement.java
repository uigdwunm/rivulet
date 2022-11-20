package zly.rivulet.mysql.generator.statement.insert;

import zly.rivulet.base.generator.param_manager.for_model_meta.ModelBatchParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.CommonParamManager;
import zly.rivulet.base.utils.CollectionUtils;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.View;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.mysql.generator.statement.SingleValueElementStatement;
import zly.rivulet.mysql.generator.statement.param.SQLParamStatement;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.insert.ColumnItemDefinition;
import zly.rivulet.sql.definition.insert.SQLInsertDefinition;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

import java.util.List;
import java.util.stream.Collectors;

public class MySQLInsertStatement extends SqlStatement {

    private final SQLInsertDefinition definition;

    private final SQLModelMeta sqlModelMeta;

    private final View<ColumnItemStatement> columnItemStatements;

    private final List<List<SingleValueElementStatement>> values;

    private final static String INSERT_INTO = "INSERT INTO ";

    private final static String VALUES = "VALUES";

    public MySQLInsertStatement(SQLInsertDefinition definition, View<ColumnItemStatement> columnItemStatements, List<List<SingleValueElementStatement>> values) {
        this.definition = definition;
        this.sqlModelMeta = definition.getSqlModelMeta();
        this.columnItemStatements = columnItemStatements;
        this.values = values;
    }

    @Override
    protected int length() {
        int length = 0;
        length += INSERT_INTO.length() + sqlModelMeta.getTableName().length() + 1;

        length += 1;

        length += columnItemStatements.size() - 1;
        for (ColumnItemStatement columnItemStatement : columnItemStatements) {
            length += columnItemStatement.getLengthOrCache();
        }

        length += 1;

        length += VALUES.length();

        length += values.size() - 1;
        for (List<SingleValueElementStatement> singleValueElementStatementList : values) {
            length += 1;
            length += singleValueElementStatementList.size() - 1;
            for (SingleValueElementStatement singleValueElementStatement : singleValueElementStatementList) {
                length += singleValueElementStatement.getLengthOrCache();
            }
            length += 1;
        }
        return length;
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(INSERT_INTO).append(sqlModelMeta.getTableName()).space();

        collector.leftBracket();
        for (ColumnItemStatement columnItemStatement : collector.createJoiner(Constant.COMMA, columnItemStatements)) {
            collector.append(columnItemStatement);
        }
        collector.rightBracket();

        collector.append(VALUES);

        for (List<SingleValueElementStatement> singleValueStatements : collector.createJoiner(Constant.COMMA, values)) {
            collector.leftBracket();
            for (SingleValueElementStatement singleValueStatement : collector.createJoiner(Constant.COMMA, singleValueStatements)) {
                singleValueStatement.singleCollectStatement(collector);
            }
            collector.rightBracket();
        }
    }


    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SQLInsertDefinition.class,
            (definition, soleFlag, toolbox) -> {
                SQLInsertDefinition sqlInsertDefinition = (SQLInsertDefinition) definition;
                List<ColumnItemStatement> columnItemStatements = sqlInsertDefinition.getColumnItemDefinitionList().stream()
                    .map(columnItemDefinition -> (ColumnItemStatement) sqlStatementFactory.warmUp(columnItemDefinition, soleFlag.subSwitch(), toolbox))
                    .collect(Collectors.toList());
                List<List<SQLSingleValueElementDefinition>> values = sqlInsertDefinition.getValues();
                List<List<SingleValueElementStatement>> valuesStatement = null;
                if (CollectionUtils.isNotEmpty(values)) {
                    valuesStatement = values.stream()
                        .map(subValues -> subValues.stream()
                            .map(sqlSingleValueElementDefinition -> (SingleValueElementStatement) sqlStatementFactory.warmUp(sqlSingleValueElementDefinition, soleFlag.subSwitch(), toolbox))
                            .collect(Collectors.toList())).collect(Collectors.toList());

                }
                return new MySQLInsertStatement(sqlInsertDefinition, View.create(columnItemStatements), valuesStatement);
            },
            (definition, toolbox) -> {
                SQLInsertDefinition sqlInsertDefinition = (SQLInsertDefinition) definition;

                List<ColumnItemDefinition> columnItemDefinitionList = sqlInsertDefinition.getColumnItemDefinitionList();
                List<ColumnItemStatement> columnItemStatements = columnItemDefinitionList.stream()
                    .map(columnItemDefinition -> (ColumnItemStatement) sqlStatementFactory.getOrCreate(columnItemDefinition, toolbox))
                    .collect(Collectors.toList());

                List<List<SQLSingleValueElementDefinition>> values = sqlInsertDefinition.getValues();
                List<List<SingleValueElementStatement>> valuesStatement;
                if (CollectionUtils.isNotEmpty(values)) {
                    valuesStatement = values.stream()
                        .map(subValues -> subValues.stream()
                            .map(sqlSingleValueElementDefinition -> (SingleValueElementStatement) sqlStatementFactory.getOrCreate(sqlSingleValueElementDefinition, toolbox))
                            .collect(Collectors.toList())).collect(Collectors.toList());
                } else {
                    ModelBatchParamManager batchParamManager = toolbox.getBatchParamManager();
                    valuesStatement = batchParamManager.getModelParamList().stream()
                        .map(model -> {
                            CommonParamManager subParamManager = batchParamManager.createCommonParamManager(model);
                            return columnItemDefinitionList.stream()
                                .map(columnItemDefinition -> (SingleValueElementStatement) new SQLParamStatement(subParamManager.getStatement(columnItemDefinition.getForModelMetaParamReceipt())))
                                .collect(Collectors.toList());
                        }).collect(Collectors.toList());
                }
                return new MySQLInsertStatement(sqlInsertDefinition, View.create(columnItemStatements), valuesStatement);
            }
        );
    }
}
