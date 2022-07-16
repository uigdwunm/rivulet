package zly.rivulet.mysql.example.config;

import zly.rivulet.base.definer.annotations.RivuletDescConfig;
import zly.rivulet.base.definer.annotations.RivuletQueryDesc;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.mysql.example.model.JoinQueryDO;
import zly.rivulet.mysql.example.model.UserDO;
import zly.rivulet.mysql.example.vo.UserJoinVO;
import zly.rivulet.mysql.example.vo.UserVO;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.describer.query.QueryBuilder;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.condition.Condition;
import zly.rivulet.sql.describer.query.desc.Mapping;

@RivuletDescConfig
public class UserDescConfig {

    @RivuletQueryDesc("sdf")
    public SqlQueryMetaDesc<UserDO, UserVO> queryUser() {
        return QueryBuilder.query(UserDO.class, UserVO.class)
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
            ).build();
    }

    @RivuletQueryDesc("")
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
