package zly.rivulet.mysql.example;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.runparser.Fish;
import zly.rivulet.base.runparser.statement.Statement;
import zly.rivulet.base.utils.FormatCollector;
import zly.rivulet.base.utils.StatementCollector;
import zly.rivulet.base.warehouse.DefaultWarehouseManager;
import zly.rivulet.mysql.MySQLRivuletManager;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.example.config.UserDescConfig;
import zly.rivulet.mysql.example.enums.UserType;
import zly.rivulet.mysql.example.model.JoinQueryDO;
import zly.rivulet.mysql.example.model.user.User;
import zly.rivulet.mysql.example.vo.UserJoinVO;
import zly.rivulet.mysql.example.vo.UserVO;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.describer.query.QueryBuilder;
import zly.rivulet.sql.describer.query.condition.Condition;
import zly.rivulet.sql.describer.query.desc.Mapping;

import java.util.Date;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) {
        User user = new User(1, "张小三", "123", 2L, 18, true, UserType.VIP, new Date(), 1, 2);

        DefaultWarehouseManager defaultWarehouseManager = new DefaultWarehouseManager("zly.rivulet.mysql");
        // todo beanManager配置扫包
        MySQLRivuletManager rivuletManager = new MySQLRivuletManager(
            new MySQLRivuletProperties(),
            new ConvertorManager(),
            defaultWarehouseManager
        );


        Fish test = rivuletManager.test(
            QueryBuilder.query(User.class, UserVO.class)
                .select(
                    Mapping.of(UserVO::setId, User::getId),
                    Mapping.of(UserVO::setName, User::getName)
                )
                .where(
                    Condition.equalTo(User::getId, Param.of(Long.class, "id", SqlParamCheckType.PLACEHOLDER)),
                    Condition.equalTo(User::getName, User::getCode),
                    Condition.or(
                        Condition.equalTo(User::getId, Param.of(Long.class, "id", SqlParamCheckType.PLACEHOLDER)),
                        Condition.equalTo(User::getName, User::getCode)
                    )
                ).build()
            );
        Statement statement = test.getStatement();
        FormatCollector formatCollector = new FormatCollector();
        statement.formatGetStatement(formatCollector);
//        System.out.println(formatCollector);

        StatementCollector collector = new StatementCollector(1000);
        statement.collectStatement(collector);
//        System.out.println(collector);

        test = rivuletManager.test(
            QueryBuilder.query(JoinQueryDO.class, UserJoinVO.class)
            .select(
                Mapping.of(UserJoinVO::setId, joinDO -> joinDO.getUserDO().getId()),
                Mapping.of(UserJoinVO::setName, joinDO -> joinDO.getUserDO().getName()),
                Mapping.of(UserJoinVO::setCityName, joinDO -> joinDO.getCityDO().getName()),
                Mapping.of(UserJoinVO::setProvinceName, joinDO -> joinDO.getProvinceDO().getName())
            ).where(
                Condition.equalTo(joinDO -> joinDO.getUserDO().getId(), Param.of(Long.class, "aa.id", SqlParamCheckType.PLACEHOLDER)),
                Condition.equalTo(joinDO -> joinDO.getUserDO().getName(), joinDO -> joinDO.getUserDO().getCode()),
                Condition.or(
                    Condition.equalTo(joinDO -> joinDO.getUserDO().getId(), Param.of(Long.class, "aa.id", SqlParamCheckType.PLACEHOLDER)),
                    Condition.equalTo(joinDO -> joinDO.getUserDO().getName(), joinDO -> joinDO.getUserDO().getCode())
                )
            ).build()
        );

        statement = test.getStatement();
        formatCollector = new FormatCollector();
        statement.formatGetStatement(formatCollector);
        System.out.println(formatCollector);

        collector = new StatementCollector(1000);
        statement.collectStatement(collector);
        System.out.println(collector);
    }

}
