package zly.rivulet.base.runparser.statement;

import zly.rivulet.base.utils.FormatCollector;
import zly.rivulet.base.utils.StatementCollector;

public interface Statement {

    void collectStatement(StatementCollector collector);

    /**
     * Description 考虑仅开发模式支持
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 14:18
     **/
    void formatGetStatement(FormatCollector collector);
}
