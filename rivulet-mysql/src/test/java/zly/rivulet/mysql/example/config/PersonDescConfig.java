package zly.rivulet.mysql.example.config;

import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.definer.annotations.RivuletDescConfig;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.mysql.example.model.PersonDO;
import zly.rivulet.sql.describer.condition.Condition;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.describer.query.QueryBuilder;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;

@RivuletDescConfig
public class PersonDescConfig {

    @RivuletDesc("sdf")
    public static SqlQueryMetaDesc<PersonDO, PersonDO> queryPerson() {
        return QueryBuilder.query(PersonDO.class, PersonDO.class)
            .select(
                Mapping.of(PersonDO::setId, PersonDO::getId),
                Mapping.of(PersonDO::setName, PersonDO::getName)
            )
            .where(
                Condition.equalTo(PersonDO::getGender, Param.of(Boolean.class, "gender", SqlParamCheckType.PLACEHOLDER)),
                Condition.or(
                    Condition.equalTo(PersonDO::getId, Param.of(Long.class, "aid", SqlParamCheckType.PLACEHOLDER)),
                    Condition.equalTo(PersonDO::getId, Param.of(Long.class, "bid", SqlParamCheckType.PLACEHOLDER)),
                    Condition.equalTo(PersonDO::getId, Param.of(Long.class, "cid", SqlParamCheckType.PLACEHOLDER))
                )
            ).build();
    }
}
