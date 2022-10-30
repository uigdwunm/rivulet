package zly.rivulet.mysql;

import zly.rivulet.sql.SqlRivuletProperties;

public class MySQLRivuletProperties extends SqlRivuletProperties {

    /**
     * 批量插入时,单条插入语句最多插入多少条
     **/
    private int batchInsertOneStatementMax = 50;

    public int getBatchInsertOneStatementMax() {
        return batchInsertOneStatementMax;
    }
}
