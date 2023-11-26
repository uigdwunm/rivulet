package zly.rivulet.mysql;

import zly.rivulet.base.describer.param.Param;
import zly.rivulet.mysql.desc.Desc_person;
import zly.rivulet.mysql.desc.Desc_subQuery_person;
import zly.rivulet.mysql.model.PersonDO;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.query_.SQLQueryMetaDesc;
import zly.rivulet.sql.describer.select.item.Mapping;
import zly.rivulet.sql.describer.select.SQLQueryBuilder;

import java.awt.*;

public class Test {

    public void test() {
        Desc_person tl = new Desc_person();
        SQLQueryMetaDesc<Desc_subQuery_person> subQuery = SQLQueryBuilder.subQuery(Desc_subQuery_person.class)
                .select(
                        tl.id,
                        Param.of(Long.class, "sdfklsf")
                )
                .from(tl)
                .where(Condition.Equal.of(tl.id, Param.staticOf(1)))
                .build();
        Desc_subQuery_person subP = new Desc_subQuery_person(subQuery);

        Desc_person tp = new Desc_person();
        SQLQueryMetaDesc<PersonDO> build = SQLQueryBuilder.mappingTo(PersonDO.class)
                .select(
                        Mapping.ofAutoConvert(PersonDO::setId, tp.name),
                        Mapping.of(PersonDO::setName, tp.name),
                        Mapping.of(PersonDO::setId, subP.id)
                ).from(tp)
                .leftJoin(subP, Condition.Equal.of(tp.id, subP.id))
                .rightJoin(subP, Condition.Equal.of(tp.id, subP.id))
                .where(
                        Condition.in.of(tp.id, Param.of(List.class, "234"))
                ).build();
    }
}
