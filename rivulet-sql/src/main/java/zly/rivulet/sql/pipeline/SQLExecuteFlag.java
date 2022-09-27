package zly.rivulet.sql.pipeline;

import zly.rivulet.base.pipeline.toolbox.ExecuteFlag;

public enum SQLExecuteFlag implements ExecuteFlag {
    BATCH_INSERT,
    BATCH_UPDATE
    ;
}
