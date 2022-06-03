package zly.rivulet.mysql;

import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.adapter.BeanManager;
import zly.rivulet.base.adapter.DefaultBeanManager;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.Definer;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.mysql.definer.MySQLDefiner;
import zly.rivulet.mysql.runparser.MysqlRunParser;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.preparser.SqlPreParser;

import java.util.Map;

public class MySQLRivuletManager extends RivuletManager {

    public MySQLRivuletManager(MySQLRivuletProperties configProperties, ConvertorManager convertorManager, BeanManager beanManager) {
        super(
            new MysqlRunParser(configProperties, convertorManager),
            null,
            null,
            configProperties,
            convertorManager,
            beanManager
        );
    }

    @Override
    protected SqlPreParser createPreParser(Map<String, WholeDesc> wholeDescMap) {
        return new SqlPreParser(
            wholeDescMap,
            new MySQLDefiner(convertorManager),
            (MySQLRivuletProperties) configProperties,
            convertorManager);
    }

}
