package zly.rivulet.mysql.pipeline;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.pipeline.ExecutePlan;
import zly.rivulet.base.pipeline.ResultInfo;

import java.sql.Connection;

public class MySQLBatchInsertExecutePlan extends ExecutePlan {
    @Override
    public Object plan(Blueprint blueprint, ParamManager paramManager, ResultInfo resultInfo, Connection connection) {
        return null;
    }
}
