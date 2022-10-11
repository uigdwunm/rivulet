package zly.rivulet.mysql.example.config;

import zly.rivulet.base.definer.annotations.RivuletDescConfig;
import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.mysql.example.enums.Rule;
import zly.rivulet.mysql.example.model.JoinQueryDO;
import zly.rivulet.mysql.example.model.user.User;
import zly.rivulet.mysql.example.vo.UserJoinVO;
import zly.rivulet.mysql.example.vo.UserVO;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.describer.query.QueryBuilder;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.query.desc.Mapping;

@RivuletDescConfig
public class UserDescConfig {

    @RivuletDesc("sdf")
    public SqlQueryMetaDesc<User, UserVO> queryUser() {
        return QueryBuilder.query(User.class, UserVO.class)
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
            ).build();
    }

    @RivuletDesc("allStudent")
    public SqlQueryMetaDesc<User, User> queryStudent() {
        return QueryBuilder.query(User.class, User.class)
            .where(Condition.equalTo(User::getRule, Param.staticOf(Rule.STUDENT)))
            .build();
    }

    @RivuletDesc("allTeacher")
    public SqlQueryMetaDesc<User, User> allTeacher() {
        return QueryBuilder.query(User.class, User.class)
            .where(Condition.equalTo(User::getRule, Param.staticOf(Rule.TEACHER)))
            .build();
    }

    @RivuletDesc("")
    public SqlQueryMetaDesc<JoinQueryDO, UserJoinVO> queryJoinUser() {
        return QueryBuilder.query(JoinQueryDO.class, UserJoinVO.class)
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
            ).build();
    }
}
