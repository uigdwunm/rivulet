package zly.rivulet.mysql.example.model;

import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.describer.query.QueryBuilder;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Condition;

public class UserDescConfig {

    public SqlQueryMetaDesc<UserDO, UserDO> queryUser() {
        return QueryBuilder.query(UserDO.class, UserDO.class)
            .where(
                Condition.equalTo(UserDO::getId, Param.of(Long.class, "aa.id", SqlParamCheckType.PLACEHOLDER)),
                Condition.equalTo(UserDO::getName, UserDO::getCode)
            ).build();
    }
}
