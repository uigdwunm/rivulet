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
import zly.rivulet.sql.definer.meta.*;
import zly.rivulet.sql.definition.function.SQLFunctionDefinition;
import zly.rivulet.sql.definition.query.SQLQueryDefinition;
import zly.rivulet.sql.definition.query.mapping.SelectItemDefinition;
import zly.rivulet.sql.definition.query.operate.OperateDefinition;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.function.SQLFunction;
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
            SQLQueryMetaDesc<?> sqlSubQueryMetaDesc = (SQLQueryMetaDesc<?>) singleValueElementDesc;
            sqlAliasManager.suggestAlias(sqlSubQueryMetaDesc, null);
            SQLQueryDefinition subSQLQuery = new SQLQueryDefinition(this, sqlSubQueryMetaDesc);
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
