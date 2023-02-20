package zly.rivulet.mysql.example.config;

import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.ParamCheckType;
import zly.rivulet.mysql.model.PersonDO;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.query.SQLQueryBuilder;
import zly.rivulet.sql.describer.query.SQLQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;

public class PersonDescConfig {

    @RivuletDesc("queryById")
    public static SQLQueryMetaDesc<PersonDO, PersonDO> queryById() {
        return SQLQueryBuilder.query(PersonDO.class, PersonDO.class)
            .where(
                Condition.Equal.of(PersonDO::getId, Param.of(Long.class, "id", ParamCheckType.PLACEHOLDER))
            ).build();
    }

    @RivuletDesc("sdf")
    public static SQLQueryMetaDesc<PersonDO, PersonDO> queryPerson() {
        return SQLQueryBuilder.query(PersonDO.class, PersonDO.class)
            .select(
                Mapping.of(PersonDO::setId, PersonDO::getId),
                Mapping.of(PersonDO::setName, PersonDO::getName)
            )
            .where(
                Condition.Equal.of(PersonDO::getGender, Param.of(Boolean.class, "gender", ParamCheckType.PLACEHOLDER)),
                Condition.or(
                    Condition.Equal.of(PersonDO::getId, Param.of(Long.class, "aid", ParamCheckType.PLACEHOLDER)),
                    Condition.Equal.of(PersonDO::getId, Param.of(Long.class, "bid", ParamCheckType.PLACEHOLDER)),
                    Condition.Equal.of(PersonDO::getId, Param.of(Long.class, "cid", ParamCheckType.PLACEHOLDER))
                )
            ).build();
    }
}
