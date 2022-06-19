package zly.rivulet.sql.runparser;

import zly.rivulet.sql.preparser.SQLAliasManager;

public class SqlRunParseInitHelper {

    private SQLAliasManager aliasManager;

    public SQLAliasManager getAliasManager() {
        return aliasManager;
    }

    public void setAliasManager(SQLAliasManager aliasManager) {
        this.aliasManager = aliasManager;
    }
}
