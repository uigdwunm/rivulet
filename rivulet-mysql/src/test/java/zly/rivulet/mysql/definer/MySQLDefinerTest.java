package zly.rivulet.mysql.definer;

import org.junit.Test;
import zly.rivulet.base.convertor.ConvertorManager;

/**
 * Description 测试目标，解析配置好的表对象，正确拿到所有字段属性
 *
 * @author zhaolaiyuan
 * Date 2022/7/31 11:05
 **/
public class MySQLDefinerTest {

    @Test
    public void test() {
        ConvertorManager convertorManager = new ConvertorManager();
        MySQLDefiner mySQLDefiner = new MySQLDefiner(convertorManager);
        mySQLDefiner.initTypeConvertor();
    }
}
