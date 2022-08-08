package zly.rivulet.sql.assembly_line.toolbox;

import zly.rivulet.sql.parser.SQLAliasManager;

public class SqlAssemblyLineWarmUpToolbox {

    private final SQLAliasManager aliasManager;

    public SqlAssemblyLineWarmUpToolbox(SQLAliasManager aliasManager) {
        this.aliasManager = aliasManager;
    }

    public SQLAliasManager getAliasManager() {
        return aliasManager;
    }

}
