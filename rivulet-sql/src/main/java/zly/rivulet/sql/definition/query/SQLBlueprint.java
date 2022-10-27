package zly.rivulet.sql.definition.query;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.generator.statement.SqlStatement;
import zly.rivulet.sql.parser.SQLAliasManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SQLBlueprint implements Blueprint {

    protected ParamReceiptManager paramReceiptManager;

    protected SQLAliasManager aliasManager;

    protected Map<Class<? extends Definition>, ParamReceipt> customStatementMap;

    /**
     * definition类和statement之间的缓存映射
     **/
    private final Map<Definition, SqlStatement> statementCache = new ConcurrentHashMap<>();

    protected boolean isWarmUp = false;


    public void setAliasManager(SQLAliasManager aliasManager) {
        this.aliasManager = aliasManager;
    }

    public SQLAliasManager getAliasManager() {
        return this.aliasManager;
    }


    @Override
    public void putStatement(Definition key, Statement sqlStatement) {
        this.statementCache.put(key, (SqlStatement) sqlStatement);
    }

    @Override
    public SqlStatement getStatement(Definition key) {
        return this.statementCache.get(key);
    }

    @Override
    public ParamReceiptManager getParamReceiptManager() {
        return paramReceiptManager;
    }

    public void setParamReceiptManager(ParamReceiptManager paramReceiptManager) {
        this.paramReceiptManager = paramReceiptManager;
    }

    @Override
    public boolean isWarmUp() {
        return this.isWarmUp;
    }

    @Override
    public void finishWarmUp() {
        this.isWarmUp = true;
    }

}
