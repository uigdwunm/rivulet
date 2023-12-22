package zly.rivulet.sql.parser.toolbox;

import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.base.parser.toolbox.ParserPortableToolbox;
import zly.rivulet.sql.SQLRivuletProperties;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.function.SQLFunctionDefinition;
import zly.rivulet.sql.definition.query.SQLQueryDefinition;
import zly.rivulet.sql.definition.query.mapping.SelectItemDefinition;
import zly.rivulet.sql.definition.query.operate.OperateDefinition;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.definer.meta.SQLColumnMeta;
import zly.rivulet.sql.definer.meta.SQLQueryMeta;
import zly.rivulet.sql.definer.meta.SQLSubQueryMeta;
import zly.rivulet.sql.definer.meta.SQLTableMeta;
import zly.rivulet.sql.describer.query_.SQLQueryMetaDesc;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.SQLParamReceiptManager;
import zly.rivulet.sql.parser.SQLParser;

import java.util.HashSet;
import java.util.Set;

public class SQLParserPortableToolbox implements ParserPortableToolbox {

    private final SQLParser sqlPreParser;

    private final ParamReceiptManager paramReceiptManager;

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

    public SQLParserPortableToolbox(SQLParser sqlPreParser) {
        this.sqlPreParser = sqlPreParser;
        this.paramReceiptManager = new SQLParamReceiptManager(sqlPreParser.getConvertorManager());
        this.configProperties = sqlPreParser.getConfigProperties();
        this.sqlAliasManager = new SQLAliasManager(sqlPreParser.getConfigProperties());
    }

    public SingleValueElementDefinition parseSingleValue(SingleValueElementDesc<?> singleValueElementDesc) {
        if (singleValueElementDesc instanceof SQLColumnMeta) {
            // 表字段类型
            return (SQLColumnMeta<?>) singleValueElementDesc;
        } else if (singleValueElementDesc instanceof SQLQueryMetaDesc) {
            // 子查询类型
            SQLQueryMetaDesc<?> sqlQueryMetaDesc = (SQLQueryMetaDesc<?>) singleValueElementDesc;
            SQLParser sqlPreParser = this.getSqlPreParser();
            return (SQLQueryDefinition) sqlPreParser.parse(sqlQueryMetaDesc, this);
        } else if (singleValueElementDesc instanceof Param) {
            // 参数类型
            Param<?> param = (Param<?>) singleValueElementDesc;
            ParamReceiptManager paramReceiptManager = this.getParamReceiptManager();
            return paramReceiptManager.registerParam(param);
        } else if (singleValueElementDesc instanceof SQLFunction) {
            SQLFunction<?> sqlFunction = (SQLFunction<?>) singleValueElementDesc;
            return new SQLFunctionDefinition(this, sqlFunction);
        } else {
            throw UnbelievableException.unknownType();
        }

    }

    /**
     * 专门用于解析SelectItem子项
     **/
    public SelectItemDefinition parseSelectItemDefinition(SingleValueElementDesc<?> singleValueElementDesc) {
        if (singleValueElementDesc instanceof SQLColumnMeta) {
            // 表字段类型
            SQLColumnMeta<?> sqlColumnMeta = (SQLColumnMeta<?>) singleValueElementDesc;
            sqlAliasManager.suggestAlias(sqlColumnMeta, sqlColumnMeta.getName());
            SQLQueryMeta sqlQueryMeta = sqlColumnMeta.getSqlQueryMeta();
            sqlAliasManager.suggestAlias(sqlQueryMeta, sqlQueryMeta.name());
            return new SelectItemDefinition(sqlColumnMeta, sqlQueryMeta);
        } else if (singleValueElementDesc instanceof SQLQueryMetaDesc) {
            // 子查询类型
            SQLQueryMetaDesc<?> sqlQueryMetaDesc = (SQLQueryMetaDesc<?>) singleValueElementDesc;
            sqlAliasManager.suggestAlias(sqlQueryMetaDesc, null);
            SQLQueryDefinition subSQLQuery = (SQLQueryDefinition) sqlPreParser.parse(sqlQueryMetaDesc, this);
            return new SelectItemDefinition(subSQLQuery, null);
        } else if (singleValueElementDesc instanceof Param) {
            // 参数类型
            Param<?> param = (Param<?>) singleValueElementDesc;
            sqlAliasManager.suggestAlias(param, null);
            ParamReceipt paramReceipt = paramReceiptManager.registerParam(param);
            return new SelectItemDefinition(paramReceipt, null);
        } else if (singleValueElementDesc instanceof SQLFunction) {
            // 函数类型
            SQLFunction<?> sqlFunction = (SQLFunction<?>) singleValueElementDesc;
            sqlAliasManager.suggestAlias(sqlFunction, null);
            SQLFunctionDefinition sqlFunctionDefinition = new SQLFunctionDefinition(this, sqlFunction);
            return new SelectItemDefinition(sqlFunctionDefinition, null);
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
            return new SQLQueryDefinition(this, sqlSubQueryMetaDesc);
        } else {
            throw UnbelievableException.unknownType();
        }
    }

    public OperateDefinition parseCondition(Condition onConditionContainer) {
        return onConditionContainer.getOperate().createDefinition(this, onConditionContainer);
    }
}
