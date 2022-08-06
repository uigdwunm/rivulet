package zly.rivulet.sql.preparser.helper;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.preparser.helper.PreParseHelper;
import zly.rivulet.base.preparser.param.ParamDefinitionManager;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.preparser.SqlParamDefinitionManager;
import zly.rivulet.sql.preparser.SqlPreParser;
import zly.rivulet.sql.preparser.helper.node.QueryProxyNode;

public class SqlPreParseHelper implements PreParseHelper {

    private final SqlPreParser sqlPreParser;

    private final ParamDefinitionManager paramDefinitionManager;

    private QueryProxyNode currNode;

    private final SqlRivuletProperties configProperties;

    public SqlPreParseHelper(SqlPreParser sqlPreParser) {
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
