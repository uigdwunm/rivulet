package zly.rivulet.sql.definition;

import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.generator.statement.SQLStatement;
import zly.rivulet.sql.parser.SQLAliasManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SQLBlueprint implements Blueprint {
    private final RivuletFlag flag;

    /**
     * Description 可能为空
     *
     * @author zhaolaiyuan
     * Date 2022/10/30 12:40
     **/
    protected final WholeDesc wholeDesc;

    protected ParamReceiptManager paramReceiptManager;

    protected SQLAliasManager aliasManager;

    protected Map<Class<? extends Definition>, ParamReceipt> customStatementMap;

    /**
     * definition类和statement之间的缓存映射
     **/
    private final Map<Definition, SQLStatement> statementCache = new ConcurrentHashMap<>();

    protected boolean isWarmUp = false;

    protected SQLBlueprint(RivuletFlag flag, WholeDesc wholeDesc) {
        this.flag = flag;
        this.wholeDesc = wholeDesc;
    }


    public void setAliasManager(SQLAliasManager aliasManager) {
        this.aliasManager = aliasManager;
    }

    public SQLAliasManager getAliasManager() {
        return this.aliasManager;
    }


    @Override
    public void putStatement(Definition key, Statement sqlStatement) {
        this.statementCache.put(key, (SQLStatement) sqlStatement);
    }

    @Override
    public SQLStatement getStatement(Definition key) {
        return this.statementCache.get(key);
    }

    @Override
    public ParamReceiptManager getParamReceiptManager() {
        return paramReceiptManager;
    }

    @Override
    public boolean isWarmUp() {
        return this.isWarmUp;
    }

    @Override
    public void finishWarmUp() {
        this.isWarmUp = true;
    }

    public Map<Class<? extends Definition>, ParamReceipt> getCustomStatementMap() {
        return customStatementMap;
    }

    public String getRivuletKey() {
        if (this.wholeDesc == null) {
            return null;
        }
        RivuletDesc annotation = wholeDesc.getAnnotation();
        if (annotation == null) {
            return null;
        }

        return annotation.value();
    }

    @Override
    public RivuletFlag getFlag() {
        return flag;
    }

    @Override
    public Class<?> getReturnType() {
        return wholeDesc.getReturnType();
    }
}
