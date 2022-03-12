package zly.rivulet.mysql;

import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.mysql.runparser.MysqlRunParser;
import zly.rivulet.sql.preparser.SqlPreParser;

public class MySQLRivuletManager extends RivuletManager {

    public MySQLRivuletManager(MySQLRivuletProperties configProperties, ConvertorManager convertorManager) {
        super(
            new SqlPreParser(configProperties, convertorManager),
            new MysqlRunParser(configProperties, convertorManager),
            analyzer,
            executor,
            configProperties,
            convertorManager
        );
    }
}
