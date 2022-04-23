package zly.rivulet.sql;

import zly.rivulet.base.RivuletProperties;

public abstract class SqlRivuletProperties extends RivuletProperties {

    /**
     * 是否执行语句尽量使用简化别名
     **/
    private boolean isTryShortAlias = true;
}
