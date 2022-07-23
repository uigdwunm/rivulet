package zly.rivulet.mysql.example;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.warehouse.DefaultWarehouseManager;
import zly.rivulet.mysql.MySQLRivuletManager;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.example.config.UserDescConfig;
import zly.rivulet.mysql.example.enums.UserType;
import zly.rivulet.mysql.example.model.UserDO;

import java.util.Date;

public class App {


    public static void main(String[] args) {
        UserDO userDO = new UserDO(1, "张小三", "123", 2L, 18, true, UserType.VIP, new Date(), 1, 2);

        DefaultWarehouseManager defaultWarehouseManager = new DefaultWarehouseManager("zly.rivulet.mysql");
        // todo beanManager配置扫包
        MySQLRivuletManager rivuletManager = new MySQLRivuletManager(
            new MySQLRivuletProperties(),
            new ConvertorManager(),
            defaultWarehouseManager
        );

        UserDescConfig userDescConfig = new UserDescConfig();

        rivuletManager.test(userDescConfig.queryUser());
        rivuletManager.test(userDescConfig.queryJoinUser());

    }

}
