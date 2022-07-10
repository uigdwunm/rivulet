package zly.rivulet.mysql.runparser.statement;

import zly.rivulet.base.utils.FormatCollectHelper;
import zly.rivulet.sql.runparser.statement.SqlStatement;

public interface SingleValueElementStatement extends SqlStatement {

    default String singleCreateStatement() {
        return createStatement();
    }

    default void singleCollectStatement(StringBuilder sqlCollector) {
        collectStatement(sqlCollector);
    }

    /**
     * Description 考虑仅开发模式支持
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 14:18
     **/
    default void singleFormatGetStatement(FormatCollectHelper formatCollectHelper) {
        singleFormatGetStatement(formatCollectHelper);
    }
}
