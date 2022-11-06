package zly.rivulet.sql.generator.toolbox;

import zly.rivulet.base.utils.PortableToolbox;
import zly.rivulet.sql.definition.SQLBlueprint;
import zly.rivulet.sql.parser.SQLAliasManager;

public class WarmUpToolbox implements PortableToolbox {

    private final SQLAliasManager aliasManager;

    private final SQLBlueprint sqlBlueprint;

    public WarmUpToolbox(SQLBlueprint sqlBlueprint) {
        this.aliasManager = sqlBlueprint.getAliasManager();
        this.sqlBlueprint = sqlBlueprint;
    }

    public SQLAliasManager getAliasManager() {
        return aliasManager;
    }

    public SQLBlueprint getSqlBlueprint() {
        return sqlBlueprint;
    }
}
