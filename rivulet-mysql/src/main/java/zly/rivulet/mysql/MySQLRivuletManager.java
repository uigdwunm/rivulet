package zly.rivulet.mysql;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.mysql.definer.MySQLDefiner;
import zly.rivulet.mysql.generator.MysqlGenerator;
import zly.rivulet.sql.SQLRivuletManager;
import zly.rivulet.sql.parser.SQLParser;

public abstract class MySQLRivuletManager extends SQLRivuletManager {

    protected MySQLRivuletManager(MySQLRivuletProperties configProperties) {
        this(configProperties, cretateDefaultConvertorManager());
    }

    protected MySQLRivuletManager(MySQLRivuletProperties configProperties, ConvertorManager convertorManager) {
        this(configProperties, new MySQLDefiner(convertorManager));
    }

    protected MySQLRivuletManager(MySQLRivuletProperties configProperties, MySQLDefiner mySQLDefiner) {
        this(configProperties, new SQLParser(mySQLDefiner, configProperties));
    }

    protected MySQLRivuletManager(MySQLRivuletProperties configProperties, SQLParser sqlParser) {
        super(new MysqlGenerator(configProperties, sqlParser));
        sqlParser.setSqlRivuletManager(this);
    }
}
