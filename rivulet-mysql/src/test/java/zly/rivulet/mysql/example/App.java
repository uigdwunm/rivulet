package zly.rivulet.mysql.example;

import zly.rivulet.base.adapter.DefaultBeanManager;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.mysql.MySQLRivuletManager;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.example.config.UserDescConfig;
import zly.rivulet.mysql.example.enums.UserType;
import zly.rivulet.mysql.example.model.UserDO;
import zly.rivulet.mysql.example.runparser.UserMapper;
import zly.rivulet.mysql.example.vo.UserVO;

import java.util.Date;

public class App {


    public static void main(String[] args) {
        UserDO userDO = new UserDO(1, "张小三", "123", 2L, 18, true, UserType.VIP, new Date(), 1, 2);

        ConvertorManager convertorManager = new ConvertorManager();


        Class<UserMapper> userMapperClass = UserMapper.class;
        Class<UserDescConfig> userDescConfigClass = UserDescConfig.class;

        DefaultBeanManager beanManager = new DefaultBeanManager();
        MySQLRivuletManager rivuletManager = new MySQLRivuletManager(
            new MySQLRivuletProperties(),
            new ConvertorManager(),
            beanManager
        );

        beanManager.register(UserDescConfig.class, rivuletManager);
        beanManager.register(UserMapper.class, rivuletManager);

        rivuletManager.preParse(
            beanManager.getAllConfigMethod(),
            beanManager.getAllProxyMethod()
        );

        UserMapper bean = beanManager.getBean(UserMapper.class);
        UserVO select = bean.select(101L);

    }

}
