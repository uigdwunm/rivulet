package zly.rivulet.sql.parser.toolbox;

import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.base.parser.toolbox.ParserPortableToolbox;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.SqlParamReceiptManager;
import zly.rivulet.sql.parser.SqlParser;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class SqlParserPortableToolbox implements ParserPortableToolbox {

    private final SqlParser sqlPreParser;

    private final ParamReceiptManager paramReceiptManager;

    /**
     * 由于可能存在嵌套的子查询，所以当前生效的node会变，这里改造成栈符合嵌套子查询层层解析的形式，
     * 完成每个子查询解析后，在外层会进行弹栈
     **/
    private final Deque<QueryProxyNode> queryProxyNodeStack = new LinkedList<>();

    private final SqlRivuletProperties configProperties;

    private final SQLAliasManager sqlAliasManager;

    /**
     * 子查询循环检测
     **/
    private final Set<WholeDesc> subQueryCycleCheck = new HashSet<>();

    public SqlParserPortableToolbox(SqlParser sqlPreParser) {
        this.sqlPreParser = sqlPreParser;
        this.paramReceiptManager = new SqlParamReceiptManager(sqlPreParser.getConvertorManager());
        this.configProperties = sqlPreParser.getConfigProperties();
        this.sqlAliasManager = new SQLAliasManager(sqlPreParser.getConfigProperties());
    }

    public SqlParser getSqlPreParser() {
        return sqlPreParser;
    }

    public ParamReceiptManager getParamReceiptManager() {
        return paramReceiptManager;
    }

    public QueryProxyNode getQueryProxyNode() {
        return this.queryProxyNodeStack.peek();
    }

    public void setQueryProxyNode(QueryProxyNode queryProxyNode) {
        this.queryProxyNodeStack.push(queryProxyNode);
    }

    public QueryProxyNode popQueryProxyNode() {
        return this.queryProxyNodeStack.pop();
    }

    public SqlRivuletProperties getConfigProperties() {
        return configProperties;
    }

    public SQLAliasManager getSqlAliasManager() {
        return sqlAliasManager;
    }

    public void checkSubQueryCycle(WholeDesc wholeDesc) {
        if (!subQueryCycleCheck.add(wholeDesc)) {
            // 循环嵌套子查询
            throw SQLDescDefineException.subQueryLoopNesting();
        }
    }

    public void finishParse(WholeDesc wholeDesc) {
        // 撤销检查
        subQueryCycleCheck.remove(wholeDesc);
    }
}
