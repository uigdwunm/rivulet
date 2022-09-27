package zly.rivulet.sql.generator.toolbox;

import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.utils.PortableToolbox;
import zly.rivulet.sql.parser.SQLAliasManager;

public class GenerateToolbox implements PortableToolbox {
    // sql长度统计
    private int length;

    private final ParamManager paramManager;

    private final SQLAliasManager aliasManager;

    public GenerateToolbox(ParamManager paramManager, SQLAliasManager aliasManager) {
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
