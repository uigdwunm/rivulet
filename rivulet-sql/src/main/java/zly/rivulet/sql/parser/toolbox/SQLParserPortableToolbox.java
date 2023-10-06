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
import zly.rivulet.sql.SQLRivuletProperties;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.function.SQLFunctionDefinition;
import zly.rivulet.sql.definition.query.operate.OperateDefinition;
import zly.rivulet.sql.definition.query_.SQLQueryDefinition;
import zly.rivulet.sql.definition.query_.mapping.MapDefinition;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.describer.meta.SQLColumnMeta;
import zly.rivulet.sql.describer.meta.SQLQueryMeta;
import zly.rivulet.sql.describer.meta.SQLSubQueryMeta;
import zly.rivulet.sql.describer.meta.SQLTableMeta;
import zly.rivulet.sql.describer.query_.SQLQueryMetaDesc;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.SQLParamReceiptManager;
import zly.rivulet.sql.parser.SQLParser;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;

import java.util.*;

public class SQLParserPortableToolbox implements ParserPortableToolbox {

    private final SQLParser sqlPreParser;

    private final ParamReceiptManager paramReceiptManager;

    /**
     * 由于可能存在嵌套的子查询，所以当前生效的node会变，这里改造成栈符合嵌套子查询层层解析的形式，
     * 完成每个子查询解析后，在外层会进行弹栈
     **/
    private final Deque<QueryProxyNode> queryProxyNodeStack = new LinkedList<>();

    private final SQLRivuletProperties configProperties;

    private final SQLAliasManager sqlAliasManager;

    /**
     * 子查询循环检测
     **/
    private final Set<WholeDesc> subQueryCycleCheck = new HashSet<>();

    /**
     * 整个语句中会可能会有多个子查询，每个子查询必须要有自己的ProxyNode，要不解析会乱套
     * 所以从缓存拿ProxyNode没问题，但是只能拿一次，如果出现重复的，一定得新建
     **/
    private final Set<SQLModelMeta> repeatProxyModelCheck = new HashSet<>();

    private final Map<Object, SQLAliasManager.AliasFlag> aliasFlagMap = new HashMap<>();

    public SQLParserPortableToolbox(SQLParser sqlPreParser) {
        this.sqlPreParser = sqlPreParser;
        this.paramReceiptManager = new SQLParamReceiptManager(sqlPreParser.getConvertorManager());
        this.configProperties = sqlPreParser.getConfigProperties();
        this.sqlAliasManager = new SQLAliasManager(sqlPreParser.getConfigProperties());
    }

    public SingleValueElementDefinition parseSingleValue(SingleValueElementDesc<?> singleValueElementDesc) {
        QueryProxyNode queryProxyNode = this.getQueryProxyNode();
        if (singleValueElementDesc instanceof SQLColumnMeta) {
            SQLColumnMeta<?> sqlColumnMeta = (SQLColumnMeta<?>) singleValueElementDesc;
            SQLAliasManager.AliasFlag aliasFlag = aliasFlagMap.get(sqlColumnMeta);
            return new MapDefinition();
        } else if (singleValueElementDesc instanceof SQLQueryMetaDesc) {
            SQLParser sqlPreParser = this.getSqlPreParser();
            SQLQueryDefinition sqlQueryDefinition = (SQLQueryDefinition) sqlPreParser.parse((SQLQueryMetaDesc<?>) singleValueElementDesc, this);
            QueryProxyNode subQueryNode = this.popQueryProxyNode();
            queryProxyNode.addConditionSubQuery(subQueryNode);
            return sqlQueryDefinition;
        } else if (singleValueElementDesc instanceof Param) {
            ParamReceiptManager paramReceiptManager = this.getParamReceiptManager();
            return paramReceiptManager.registerParam((Param<?>) singleValueElementDesc);
        } else if (singleValueElementDesc instanceof SQLFunction) {
            SQLFunction<?> sqlFunction = (SQLFunction<?>) singleValueElementDesc;
            return new SQLFunctionDefinition(this, sqlFunction);
        } else {
            throw UnbelievableException.unknownType();
        }

    }


    public MapDefinition parseSingValueForSelect(
        Object proxyModel,
        SingleValueElementDesc<?> singleValueElementDesc
    ) {
        QueryProxyNode queryProxyNode = this.getQueryProxyNode();
        if (singleValueElementDesc instanceof FieldMapping) {
            // 字段类的select
            return queryProxyNode.getFieldDefinitionFromThreadLocal((FieldMapping<?, ?>) singleValueElementDesc, proxyModel);
        } else if (singleValueElementDesc instanceof SQLQueryMetaDesc) {
            // 子查询类型的select
            sqlPreParser.parse((WholeDesc) singleValueElementDesc, this);
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
            SQLFunction<?> sqlFunction = (SQLFunction<?>) singleValueElementDesc;
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

    public SingleValueElementDefinition parseSingleValueForCondition(SingleValueElementDesc<?> singleValueElementDesc) {
        QueryProxyNode queryProxyNode = this.getQueryProxyNode();
        if (singleValueElementDesc instanceof FieldMapping) {
            FieldMapping<Object, Object> fieldMapping = (FieldMapping<Object, Object>) singleValueElementDesc;
            return queryProxyNode.getFieldDefinitionFromThreadLocal(fieldMapping, queryProxyNode.getProxyModel());
        } else if (singleValueElementDesc instanceof JoinFieldMapping) {
            JoinFieldMapping<Object> joinFieldMapping = (JoinFieldMapping<Object>) singleValueElementDesc;
            return queryProxyNode.getFieldDefinitionFromThreadLocal(joinFieldMapping, queryProxyNode.getProxyModel());
        } else if (singleValueElementDesc instanceof SQLQueryMetaDesc) {
            SQLParser sqlPreParser = this.getSqlPreParser();
            SQLQueryDefinition sqlQueryDefinition = (SQLQueryDefinition) sqlPreParser.parse((SQLQueryMetaDesc<?>) singleValueElementDesc, this);
            QueryProxyNode subQueryNode = this.popQueryProxyNode();
            queryProxyNode.addConditionSubQuery(subQueryNode);
            return sqlQueryDefinition;
        } else if (singleValueElementDesc instanceof Param) {
            ParamReceiptManager paramReceiptManager = this.getParamReceiptManager();
            return paramReceiptManager.registerParam((Param<?>) singleValueElementDesc);
        } else if (singleValueElementDesc instanceof SQLFunction) {
            SQLFunction<?> sqlFunction = (SQLFunction<?>) singleValueElementDesc;
            return new SQLFunctionDefinition(this, sqlFunction);
        } else {
            throw UnbelievableException.unknownType();
        }
    }

    /**
     * Description custom解析singleValue仅支持FieldMapping
     *
     * @author zhaolaiyuan
     * Date 2022/10/23 13:51
     **/
    public static SingleValueElementDefinition parseSingleValueForCustom(
        QueryProxyNode queryProxyNode,
        SingleValueElementDesc<?> singleValueElementDesc
    ) {
        if (singleValueElementDesc instanceof FieldMapping) {
            FieldMapping<Object, Object> fieldMapping = (FieldMapping<Object, Object>) singleValueElementDesc;
            return queryProxyNode.getFieldDefinitionFromThreadLocal(fieldMapping, queryProxyNode.getProxyModel());
        } else {
            throw UnbelievableException.unknownType();
        }
    }

    public SQLParser getSqlPreParser() {
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

    public SQLRivuletProperties getConfigProperties() {
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

    /**
     * Description 重复检查，true是检查通过，false是不通过存在重复
     *
     * @author zhaolaiyuan
     * Date 2022/10/27 8:41
     **/
    public boolean repeatProxyNodeCheck(SQLModelMeta sqlModelMeta) {
        return repeatProxyModelCheck.add(sqlModelMeta);
    }

    public QueryFromMeta parseQueryFromMeta(SQLQueryMeta sqlQueryMeta) {
        if (sqlQueryMeta instanceof SQLTableMeta) {
            return (SQLTableMeta) sqlQueryMeta;
        } else if (sqlQueryMeta instanceof SQLSubQueryMeta) {
            SQLSubQueryMeta sqlSubQueryMeta = (SQLSubQueryMeta) sqlQueryMeta;
            SQLQueryMetaDesc<?> sqlSubQueryMetaDesc = sqlSubQueryMeta.getSqlSubQueryMetaDesc();
            return new zly.rivulet.sql.definition.query.SQLQueryDefinition(this, sqlSubQueryMetaDesc);
        } else {
            throw UnbelievableException.unknownType();
        }
    }

    public OperateDefinition parseCondition(ConditionContainer onConditionContainer) {
        return onConditionContainer.getOperate().createDefinition(this, onConditionContainer);
    }
}
