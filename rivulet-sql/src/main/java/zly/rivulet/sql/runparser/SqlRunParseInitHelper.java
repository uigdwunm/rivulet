package zly.rivulet.sql.runparser;

import zly.rivulet.base.preparser.param.ParamDefinitionManager;
import zly.rivulet.sql.preparser.SQLAliasManager;

public class SqlRunParseInitHelper {

    private final SQLAliasManager aliasManager;

    public SqlRunParseInitHelper(SQLAliasManager aliasManager) {
        this.aliasManager = aliasManager;
    }

    public SQLAliasManager getAliasManager() {
        return aliasManager;
    }

}
