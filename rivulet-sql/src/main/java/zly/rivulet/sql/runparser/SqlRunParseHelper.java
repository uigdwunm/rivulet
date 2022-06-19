package zly.rivulet.sql.runparser;

import zly.rivulet.base.runparser.param_manager.ParamManager;
import zly.rivulet.sql.preparser.SQLAliasManager;

public class SqlRunParseHelper {
    // sql长度统计
    private int length;

    private final ParamManager paramManager;

    private SQLAliasManager aliasManager;

    public SqlRunParseHelper(ParamManager paramManager) {
        this.paramManager = paramManager;
    }

    public int getLength() {
        return length;
    }

    public void addLength(int length) {
        this.length += length;
    }

    public ParamManager getParamManager() {
        return paramManager;
    }

    public SQLAliasManager getAliasManager() {
        return aliasManager;
    }

    public void setAliasManager(SQLAliasManager aliasManager) {
        this.aliasManager = aliasManager;
    }
}
