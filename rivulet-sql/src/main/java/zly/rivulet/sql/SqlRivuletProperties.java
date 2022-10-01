package zly.rivulet.sql;

import zly.rivulet.base.RivuletProperties;

public abstract class SqlRivuletProperties extends RivuletProperties {

    /**
     * 是否执行语句尽量使用简化别名
     **/
    private boolean useShortAlias = true;

    /**
     * 单个语句嵌套子查询的最大值（避免出现死循环嵌套）
     **/
    private int subQueryMax = 100;

    public boolean isUseShortAlias() {
        return useShortAlias;
    }

    public int getSubQueryMax() {
        return subQueryMax;
    }

    public void setSubQueryMax(int subQueryMax) {
        this.subQueryMax = subQueryMax;
    }

    public void setUseShortAlias(boolean useShortAlias) {
        this.useShortAlias = useShortAlias;
    }
}
