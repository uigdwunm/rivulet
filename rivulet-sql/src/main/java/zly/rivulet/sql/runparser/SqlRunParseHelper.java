package zly.rivulet.sql.runparser;

import zly.rivulet.base.runparser.param_manager.ParamManager;

public class SqlRunParseHelper {
    // sql长度统计
    private int length;

    private final ParamManager paramManager;

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
}
