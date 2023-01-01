package zly.rivulet.mysql;

import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.exception.ExecuteException;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.utils.CollectionUtils;
import zly.rivulet.mysql.pipeline.plan.MySQLBatchInsertExecutePlan;
import zly.rivulet.mysql.pipeline.plan.MySQLBatchUpdateExecutePlan;
import zly.rivulet.sql.SQLRivulet;
import zly.rivulet.sql.SQLRivuletManager;
import zly.rivulet.sql.definition.SQLBlueprint;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

public abstract class MySQLRivulet extends SQLRivulet {

    protected MySQLRivulet(SQLRivuletManager rivuletManager, Connection connection) {
        super(rivuletManager, connection);
    }

    @Override
    public <T> List<Integer> batchInsert(Collection<T> models, Class<T> dOModelClass) {
        if (CollectionUtils.isEmpty(models)) {
            throw ExecuteException.execError("没有需要插入的数据");
        }
        ModelMeta modelMeta = definer.createOrGetModelMeta(dOModelClass);
        SQLBlueprint sqlBlueprint = (SQLBlueprint) parser.parseInsertByMeta(modelMeta);

        MySQLRivuletProperties rivuletProperties = this.getRivuletProperties();
        MySQLBatchInsertExecutePlan executePlan = new MySQLBatchInsertExecutePlan(rivuletProperties, super.useConnection());

        ParamManager paramManager = paramManagerFactory.getBatchByModelMeta(modelMeta, (Collection<Object>) models);
        return runningPipeline.go(sqlBlueprint, paramManager, executePlan);
    }

    private MySQLRivuletProperties getRivuletProperties() {
        return (MySQLRivuletProperties) super.rivuletProperties;
    }

    @Override
    public <T> int batchUpdateById(Collection<T> models, Class<T> dOModelClass) {
        if (CollectionUtils.isEmpty(models)) {
            throw ExecuteException.execError("没有需要插入的数据");
        }
        ModelMeta modelMeta = definer.createOrGetModelMeta(dOModelClass);
        SQLBlueprint sqlBlueprint = (SQLBlueprint) parser.parseUpdateByMeta(modelMeta);

        MySQLBatchUpdateExecutePlan executePlan = new MySQLBatchUpdateExecutePlan(super.useConnection());

        ParamManager paramManager = paramManagerFactory.getBatchByModelMeta(modelMeta, (Collection<Object>) models);
        return runningPipeline.go(sqlBlueprint, paramManager, executePlan);
    }
}
