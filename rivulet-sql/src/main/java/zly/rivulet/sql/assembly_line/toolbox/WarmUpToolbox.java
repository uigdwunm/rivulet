package zly.rivulet.sql.assembly_line.toolbox;

import zly.rivulet.base.utils.PortableToolbox;
import zly.rivulet.sql.parser.SQLAliasManager;

public class WarmUpToolbox implements PortableToolbox {

    private final SQLAliasManager aliasManager;

    public WarmUpToolbox(SQLAliasManager aliasManager) {
        this.aliasManager = aliasManager;
    }

    public SQLAliasManager getAliasManager() {
        return aliasManager;
    }

}
