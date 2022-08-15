package zly.rivulet.sql.definition.update;

import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.sql.definition.query.main.WhereDefinition;
import zly.rivulet.sql.describer.update.SqlUpdateMetaDesc;
import zly.rivulet.sql.parser.node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class SqlUpdateDefinition {

    private final SetDefinition setDefinition;

    private final WhereDefinition whereDefinition;

    public SqlUpdateDefinition(SqlParserPortableToolbox sqlPreParseHelper, WholeDesc wholeDesc) {
        SqlUpdateMetaDesc<?> metaDesc = (SqlUpdateMetaDesc<?>) wholeDesc;
        QueryProxyNode queryProxyNode = new QueryProxyNode(sqlPreParseHelper, metaDesc.getMainFrom());


    }
}