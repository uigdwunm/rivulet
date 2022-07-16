package zly.rivulet.sql.runparser.statement;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.runparser.statement.Statement;
import zly.rivulet.base.utils.FormatCollectHelper;

public interface SqlStatement extends Statement {

    String createStatement();

    void collectStatement(StringBuilder sqlCollector);

    /**
     * Description 考虑仅开发模式支持
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 14:18
     **/
    void formatGetStatement(FormatCollectHelper formatCollectHelper);

}
