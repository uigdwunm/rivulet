package zly.rivulet.sql.parser.toolbox;

import zly.rivulet.base.parser.toolbox.ParserPortableToolbox;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.parser.SqlParamReceiptManager;
import zly.rivulet.sql.parser.SqlParser;
import zly.rivulet.sql.parser.node.QueryProxyNode;

public class SqlParserPortableToolbox implements ParserPortableToolbox {

    private final SqlParser sqlPreParser;

    private final ParamReceiptManager paramReceiptManager;

    private QueryProxyNode currNode;

    private final SqlRivuletProperties configProperties;

    private int subQueryCount = 0;

    public SqlParserPortableToolbox(SqlParser sqlPreParser) {
        this.sqlPreParser = sqlPreParser;
        this.paramReceiptManager = new SqlParamReceiptManager(sqlPreParser.getConvertorManager());
        this.configProperties = sqlPreParser.getConfigProperties();
    }

    public int incrSubQuery() {
        return ++this.subQueryCount;
    }

    public SqlParser getSqlPreParser() {
        return sqlPreParser;
    }

    public ParamReceiptManager getParamDefinitionManager() {
        return paramReceiptManager;
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
