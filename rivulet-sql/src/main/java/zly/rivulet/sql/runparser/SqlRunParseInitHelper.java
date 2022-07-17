package zly.rivulet.sql.runparser;

import zly.rivulet.base.preparser.param.ParamDefinitionManager;
import zly.rivulet.sql.preparser.SQLAliasManager;

public class SqlRunParseInitHelper {

    private final ParamDefinitionManager paramDefinitionManager;


    private final SQLAliasManager aliasManager;

    public SqlRunParseInitHelper(ParamDefinitionManager paramDefinitionManager, SQLAliasManager aliasManager) {
        this.paramDefinitionManager = paramDefinitionManager;
        this.aliasManager = aliasManager;
    }

    public SQLAliasManager getAliasManager() {
        return aliasManager;
    }

    public ParamDefinitionManager getParamDefinitionManager() {
        return paramDefinitionManager;
    }
}
