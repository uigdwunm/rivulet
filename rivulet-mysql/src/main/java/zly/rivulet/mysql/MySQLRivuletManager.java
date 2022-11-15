package zly.rivulet.mysql;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.exception.ExecuteException;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.utils.CollectionUtils;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.mysql.definer.MySQLDefiner;
import zly.rivulet.mysql.generator.MysqlGenerator;
import zly.rivulet.mysql.pipeline.plan.MySQLBatchInsertExecutePlan;
import zly.rivulet.mysql.pipeline.plan.MySQLBatchUpdateExecutePlan;
import zly.rivulet.sql.SQLRivuletManager;
import zly.rivulet.sql.definition.SQLBlueprint;
import zly.rivulet.sql.parser.SqlParser;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;

public class MySQLRivuletManager extends SQLRivuletManager {

    public MySQLRivuletManager(
        MySQLRivuletProperties configProperties,
        ConvertorManager convertorManager,
        WarehouseManager warehouseManager,
        DataSource dataSource
    ) {
        super(
            new MysqlGenerator(
                configProperties,
                new SqlParser(
                    warehouseManager,
                    new MySQLDefiner(convertorManager),
                    configProperties,
                    convertorManager
                )
            ),
            warehouseManager,
            dataSource
        );
    }

    @Override
    public <T> List<Integer> batchInsert(Connection connection, Collection<T> models, Class<T> dOModelClass) {
        if (CollectionUtils.isEmpty(models)) {
            throw ExecuteException.execError("没有需要插入的数据");
        }
        ModelMeta modelMeta = definer.createOrGetModelMeta(dOModelClass);
        SQLBlueprint sqlBlueprint = (SQLBlueprint) parser.parseInsertByMeta(modelMeta);

        MySQLRivuletProperties rivuletProperties = this.getRivuletProperties();
        MySQLBatchInsertExecutePlan executePlan = new MySQLBatchInsertExecutePlan(rivuletProperties, connection);

        ParamManager paramManager = paramManagerFactory.getBatchByModelMeta(modelMeta, (Collection<Object>) models);
        return runningPipeline.go(sqlBlueprint, paramManager, executePlan);
    }

    @Override
    public <T> int batchUpdateById(Connection connection, Collection<T> models, Class<T> dOModelClass) {
        if (CollectionUtils.isEmpty(models)) {
            throw ExecuteException.execError("没有需要插入的数据");
        }
        ModelMeta modelMeta = definer.createOrGetModelMeta(dOModelClass);
        SQLBlueprint sqlBlueprint = (SQLBlueprint) parser.parseUpdateByMeta(modelMeta);

        MySQLRivuletProperties rivuletProperties = this.getRivuletProperties();
        MySQLBatchUpdateExecutePlan executePlan = new MySQLBatchUpdateExecutePlan(connection);

        ParamManager paramManager = paramManagerFactory.getBatchByModelMeta(modelMeta, (Collection<Object>) models);
        return runningPipeline.go(sqlBlueprint, paramManager, executePlan);
    }

    public MySQLRivuletProperties getRivuletProperties() {
        return (MySQLRivuletProperties) this.configProperties;
    }
}
