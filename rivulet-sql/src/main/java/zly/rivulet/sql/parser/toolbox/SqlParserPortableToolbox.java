package zly.rivulet.sql.parser.toolbox;

import zly.rivulet.base.parser.toolbox.ParserPortableToolbox;
import zly.rivulet.base.parser.param.ParamDefinitionManager;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.parser.SqlParamDefinitionManager;
import zly.rivulet.sql.parser.SqlPreParser;
import zly.rivulet.sql.parser.node.QueryProxyNode;

public class SqlParserPortableToolbox implements ParserPortableToolbox {

    private final SqlPreParser sqlPreParser;

    private final ParamDefinitionManager paramDefinitionManager;

    private QueryProxyNode currNode;

    private final SqlRivuletProperties configProperties;

    public SqlParserPortableToolbox(SqlPreParser sqlPreParser) {
        this.sqlPreParser = sqlPreParser;
        this.paramDefinitionManager = new SqlParamDefinitionManager(sqlPreParser.getConvertorManager());
        this.configProperties = sqlPreParser.getConfigProperties();
    }

    public SqlPreParser getSqlPreParser() {
        return sqlPreParser;
    }

    public ParamDefinitionManager getParamDefinitionManager() {
        return paramDefinitionManager;
    }

    public QueryProxyNode getCurrNode() {
        return currNode;
    }

    public void setCurrNode(QueryProxyNode currNode) {
        this.currNode = currNode;
    }

    public SqlRivuletProperties getConfigProperties() {
        return configProperties;
    }
}
