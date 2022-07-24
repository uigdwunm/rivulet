package zly.rivulet.mysql.example;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.runparser.Fish;
import zly.rivulet.base.runparser.statement.Statement;
import zly.rivulet.base.warehouse.DefaultWarehouseManager;
import zly.rivulet.mysql.MySQLRivuletManager;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.example.config.UserDescConfig;
import zly.rivulet.mysql.example.enums.UserType;
import zly.rivulet.mysql.example.model.UserDO;
import zly.rivulet.mysql.example.vo.UserVO;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.describer.query.QueryBuilder;
import zly.rivulet.sql.describer.query.condition.Condition;
import zly.rivulet.sql.describer.query.desc.Mapping;

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

        Fish test = rivuletManager.test(
            QueryBuilder.query(UserDO.class, UserVO.class)
                .select(
                    Mapping.of(UserVO::setId, UserDO::getId),
                    Mapping.of(UserVO::setName, UserDO::getName)
                )
                .where(
                    Condition.equalTo(UserDO::getId, Param.of(Long.class, "id", SqlParamCheckType.PLACEHOLDER)),
                    Condition.equalTo(UserDO::getName, UserDO::getCode),
                    Condition.or(
                        Condition.equalTo(UserDO::getId, Param.of(Long.class, "id", SqlParamCheckType.PLACEHOLDER)),
                        Condition.equalTo(UserDO::getName, UserDO::getCode)
                    )
                ).build()
            );
        Statement statement = test.getStatement();
        String statement1 = statement.createStatement();
        System.out.println(statement1);

        rivuletManager.test(userDescConfig.queryJoinUser());

    }

}
