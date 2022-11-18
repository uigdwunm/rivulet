package zly.rivulet.mysql.example;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.base.utils.collector.FixedLengthStatementCollector;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.base.warehouse.DefaultWarehouseManager;
import zly.rivulet.mysql.MySQLRivuletManager;
import zly.rivulet.mysql.MySQLRivuletProperties;
import zly.rivulet.mysql.discriber.function.MySQLFunction;
import zly.rivulet.mysql.example.enums.UserType;
import zly.rivulet.mysql.example.model.JoinQueryDO;
import zly.rivulet.mysql.example.model.user.User;
import zly.rivulet.mysql.example.vo.UserJoinVO;
import zly.rivulet.mysql.example.vo.UserVO;
import zly.rivulet.mysql.util.MySQLFormatStatementCollector;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.describer.query.QueryBuilder;
import zly.rivulet.sql.describer.query.desc.Mapping;

import java.util.Date;

public class App {

    public static void main(String[] args) {
        User user = new User(1, "张小三", 18, true, UserType.VIP, new Date(), 1, 2);

        DefaultWarehouseManager defaultWarehouseManager = new DefaultWarehouseManager("zly.rivulet.mysql");
        // todo beanManager配置扫包
        MySQLRivuletManager rivuletManager = new MySQLRivuletManager(
            new MySQLRivuletProperties(),
            new ConvertorManager(),
            defaultWarehouseManager,
            null
        );

        Fish test = rivuletManager.testParse(
            QueryBuilder.query(User.class, UserVO.class)
                .select(
                    Mapping.of(UserVO::setId, MySQLFunction.Arithmetical.add(User::getId)),
                    Mapping.of(UserVO::setId, MySQLFunction.Arithmetical.add(User::getId, MySQLFunction.Arithmetical.add(User::getId), MySQLFunction.Arithmetical.add(User::getId))),
                    Mapping.of(UserVO::setId, User::getId),
                    Mapping.of(UserVO::setName, User::getName)
                )
                .where(
                    Condition.equalTo(User::getName, User::getName),
                    Condition.or(
                        Condition.equalTo(User::getId, Param.of(Long.class, "id", SqlParamCheckType.PLACEHOLDER)),
                        Condition.equalTo(User::getName, Param.of(String.class, "name", SqlParamCheckType.NATURE))
                    )
                ).build()
            );
        Statement statement = test.getStatement();
        MySQLFormatStatementCollector formatStatementCollector = new MySQLFormatStatementCollector();
        statement.collectStatement(formatStatementCollector);
        System.out.println(formatStatementCollector);

        StatementCollector collector = new FixedLengthStatementCollector(test.getLength());
        statement.collectStatement(collector);
        System.out.println(collector);

        test = rivuletManager.testParse(
            QueryBuilder.query(JoinQueryDO.class, UserJoinVO.class)
            .select(
                Mapping.of(UserJoinVO::setId, joinDO -> joinDO.getUserDO().getId()),
                Mapping.of(UserJoinVO::setName, joinDO -> joinDO.getUserDO().getName()),
                Mapping.of(UserJoinVO::setCityName, joinDO -> joinDO.getCityDO().getName()),
                Mapping.of(UserJoinVO::setProvinceName, joinDO -> joinDO.getProvinceDO().getName())
            ).where(
                Condition.equalTo(joinDO -> joinDO.getUserDO().getId(), Param.of(Long.class, "aa.id", SqlParamCheckType.PLACEHOLDER)),
                Condition.equalTo(joinDO -> joinDO.getUserDO().getName(), joinDO -> joinDO.getUserDO().getAge()),
                Condition.or(
                    Condition.equalTo(joinDO -> joinDO.getUserDO().getId(), Param.of(Long.class, "aa.id", SqlParamCheckType.PLACEHOLDER)),
                    Condition.equalTo(joinDO -> joinDO.getUserDO().getName(), joinDO -> joinDO.getUserDO().getBirthday())
                )
            ).build()
        );

        statement = test.getStatement();
        formatStatementCollector = new MySQLFormatStatementCollector();
        System.out.println(formatStatementCollector);

        collector = new FixedLengthStatementCollector(test.getLength());
        statement.collectStatement(collector);
        System.out.println(collector);
    }

}
