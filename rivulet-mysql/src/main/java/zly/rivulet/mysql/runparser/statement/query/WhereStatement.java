package zly.rivulet.mysql.runparser.statement.query;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.utils.FormatCollectHelper;
import zly.rivulet.sql.runparser.statement.SqlStatement;

public class WhereStatement implements SqlStatement {
    @Override
    public String createStatement() {
        return null;
    }

    @Override
    public void collectStatement(StringBuilder sqlCollector) {

    }

    @Override
    public void formatGetStatement(FormatCollectHelper formatCollectHelper) {
    }

    @Override
    public Definition getOriginDefinition() {
        return null;
    }
}
