package zly.rivulet.sql.parser.toolbox;

import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.base.parser.toolbox.ParserPortableToolbox;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.definition.function.SQLFunctionDefinition;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
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



    public MapDefinition parseSingValueForSelect(
        Object proxyModel,
        SingleValueElementDesc<?, ?> singleValueElementDesc
    ) {
        QueryProxyNode queryProxyNode = this.getQueryProxyNode();
        if (singleValueElementDesc instanceof FieldMapping) {
            // 字段类的select
            return queryProxyNode.getFieldDefinitionFromThreadLocal((FieldMapping<?, ?>) singleValueElementDesc, proxyModel);
        } else if (singleValueElementDesc instanceof SqlQueryMetaDesc) {
            // 子查询类型的select
            sqlPreParser.parseByDesc((WholeDesc) singleValueElementDesc, this);
            QueryProxyNode subQueryProxyNode = this.popQueryProxyNode();
            return new MapDefinition(
                subQueryProxyNode.getQuerySelectMeta(),
                null,
                subQueryProxyNode.getAliasFlag()
            );

        } else if (singleValueElementDesc instanceof Param) {
            // 参数类型的select
            ParamReceiptManager paramReceiptManager = this.getParamReceiptManager();
            ParamReceipt paramReceipt = paramReceiptManager.registerParam((Param<?>) singleValueElementDesc);
            return new MapDefinition(
                paramReceipt,
                null,
                SQLAliasManager.createAlias()
            );

        } else if (singleValueElementDesc instanceof SQLFunction) {
            SQLFunction<?, ?> sqlFunction = (SQLFunction<?, ?>) singleValueElementDesc;
            SQLFunctionDefinition sqlFunctionDefinition = new SQLFunctionDefinition(this, sqlFunction);
            return new MapDefinition(
                sqlFunctionDefinition,
                null,
                SQLAliasManager.createAlias()
            );
        } else {
            throw UnbelievableException.unknownType();
        }
    }

    public SingleValueElementDefinition parseSingleValueForCondition(SingleValueElementDesc<?, ?> singleValueElementDesc) {
        QueryProxyNode queryProxyNode = this.getQueryProxyNode();
        if (singleValueElementDesc instanceof FieldMapping) {
            FieldMapping<Object, Object> fieldMapping = (FieldMapping<Object, Object>) singleValueElementDesc;
            return queryProxyNode.getFieldDefinitionFromThreadLocal(fieldMapping, queryProxyNode.getProxyModel());
        } else if (singleValueElementDesc instanceof JoinFieldMapping) {
            JoinFieldMapping<Object> joinFieldMapping = (JoinFieldMapping<Object>) singleValueElementDesc;
            return queryProxyNode.getFieldDefinitionFromThreadLocal(joinFieldMapping, queryProxyNode.getProxyModel());
        } else if (singleValueElementDesc instanceof SqlQueryMetaDesc) {
            SqlParser sqlPreParser = this.getSqlPreParser();
            SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) sqlPreParser.parseByDesc((SqlQueryMetaDesc<?, ?>) singleValueElementDesc, this);
            QueryProxyNode subQueryNode = this.popQueryProxyNode();
            queryProxyNode.addConditionSubQuery(subQueryNode);
            return sqlQueryDefinition;
        } else if (singleValueElementDesc instanceof Param) {
            ParamReceiptManager paramReceiptManager = this.getParamReceiptManager();
            return paramReceiptManager.registerParam((Param<?>) singleValueElementDesc);
        } else if (singleValueElementDesc instanceof SQLFunction) {
            SQLFunction<?, ?> sqlFunction = (SQLFunction<?, ?>) singleValueElementDesc;
            return new SQLFunctionDefinition(this, sqlFunction);
        }
        throw UnbelievableException.unknownType();
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
