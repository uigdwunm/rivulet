package zly.rivulet.mysql;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.exception.ExecuteException;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.utils.CollectionUtils;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.mysql.definer.MySQLDefiner;
import zly.rivulet.mysql.generator.MysqlGenerator;
import zly.rivulet.sql.SQLRivuletManager;
import zly.rivulet.sql.definition.SQLBlueprint;
import zly.rivulet.sql.parser.SqlParser;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    public <T> List<Integer> batchInsert(Collection<T> models, Class<T> dOModelClass) {
        if (CollectionUtils.isEmpty(models)) {
            throw ExecuteException.execError("没有需要插入的数据");
        }
        ModelMeta modelMeta = definer.createOrGetModelMeta(dOModelClass);
        SQLBlueprint sqlBlueprint = (SQLBlueprint) parser.parseInsertByMeta(modelMeta);


        MySQLRivuletProperties rivuletProperties = this.getRivuletProperties();
        try (Connection connection = this.getConnection()) {

            List<Integer> result = new ArrayList<>();
            List<Object> subList = new ArrayList<>(rivuletProperties.getBatchInsertOneStatementMax());
            for (T model : models) {
                subList.add(model);
                if (subList.size() == rivuletProperties.getBatchInsertOneStatementMax()) {
                    ParamManager paramManager = paramManagerFactory.getBatchByModelMeta(modelMeta, subList);
                    int[] ints = this.executeBatchUpdate(connection, sqlBlueprint, paramManager, false);
                    result.addAll(Arrays.stream(ints).boxed().collect(Collectors.toList()));
                    subList.clear();
                }
            }
            ParamManager paramManager = paramManagerFactory.getBatchByModelMeta(modelMeta, subList);
            int[] ints = this.executeBatchUpdate(connection, sqlBlueprint, paramManager, true);
            result.addAll(Arrays.stream(ints).boxed().collect(Collectors.toList()));

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public MySQLRivuletProperties getRivuletProperties() {
        return (MySQLRivuletProperties) this.configProperties;
    }
}
