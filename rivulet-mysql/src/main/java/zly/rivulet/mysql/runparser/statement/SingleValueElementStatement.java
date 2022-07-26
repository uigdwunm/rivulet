package zly.rivulet.mysql.runparser.statement;

import zly.rivulet.base.utils.FormatCollector;
import zly.rivulet.base.utils.StatementCollector;
import zly.rivulet.sql.runparser.statement.SqlStatement;

public interface SingleValueElementStatement extends SqlStatement {


    default void singleCollectStatement(StatementCollector collector) {
        collectStatement(collector);
    }

    /**
     * Description 考虑仅开发模式支持
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 14:18
     **/
    default void singleFormatGetStatement(FormatCollector collector) {
        formatGetStatement(collector);
    }
}
