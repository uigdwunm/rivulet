package zly.rivulet.sql.runparser;

import zly.rivulet.base.assembly_line.param_manager.ParamManager;
import zly.rivulet.sql.preparser.SQLAliasManager;

public class SqlRunParseHelper {
    // sql长度统计
    private int length;

    private final ParamManager paramManager;

    private final SQLAliasManager aliasManager;

    public SqlRunParseHelper(ParamManager paramManager, SQLAliasManager aliasManager) {
        this.paramManager = paramManager;
        this.aliasManager = aliasManager;
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
}
