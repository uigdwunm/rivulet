package zly.rivulet.mysql.example.config;

import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.ParamCheckType;
import zly.rivulet.mysql.model.PersonDO;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.query.QueryBuilder;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;

public class PersonDescConfig {

    @RivuletDesc("queryById")
    public static SqlQueryMetaDesc<PersonDO, PersonDO> queryById() {
        return QueryBuilder.query(PersonDO.class, PersonDO.class)
            .where(
                Condition.equalTo(PersonDO::getId, Param.of(Long.class, "id", ParamCheckType.PLACEHOLDER))
            ).build();
    }

    @RivuletDesc("sdf")
    public static SqlQueryMetaDesc<PersonDO, PersonDO> queryPerson() {
        return QueryBuilder.query(PersonDO.class, PersonDO.class)
            .select(
                Mapping.of(PersonDO::setId, PersonDO::getId),
                Mapping.of(PersonDO::setName, PersonDO::getName)
            )
            .where(
                Condition.equalTo(PersonDO::getGender, Param.of(Boolean.class, "gender", ParamCheckType.PLACEHOLDER)),
                Condition.or(
                    Condition.equalTo(PersonDO::getId, Param.of(Long.class, "aid", ParamCheckType.PLACEHOLDER)),
                    Condition.equalTo(PersonDO::getId, Param.of(Long.class, "bid", ParamCheckType.PLACEHOLDER)),
                    Condition.equalTo(PersonDO::getId, Param.of(Long.class, "cid", ParamCheckType.PLACEHOLDER))
                )
            ).build();
    }
}
