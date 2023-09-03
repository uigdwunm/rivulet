package zly.rivulet.mysql;

import zly.rivulet.base.describer.param.Param;
import zly.rivulet.mysql.desc.Desc_person;
import zly.rivulet.mysql.model.PersonDO;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.select.item.Mapping;
import zly.rivulet.sql.describer.select.SQLQueryBuilder;

import java.awt.*;

public class Test {

    public void test() {
        Desc_person tp = new Desc_person();
        Desc_person tl = new Desc_person();

        SQLQueryBuilder.mappingTo(PersonDO.class)
                .selectAutoFillModel(
                        Mapping.of(PersonDO::setId, tp.id),
                        Mapping.of(PersonDO::setName, tp.name)
                ).from(tp)
                .leftJoin(tl, Condition.Equal.of(tp.id, tl.id))
                .rightJoin(tl, Condition.Equal.of(tp.id, tl.id))
                .where(
                        Condition.in.of(tp.id, Param.of(List.class, "234"))
                );
    }
}
