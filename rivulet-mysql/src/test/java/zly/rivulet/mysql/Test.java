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
        // 一个子查询
        Desc_person tl = new Desc_person();
        SQLQueryMetaDesc<Desc_subQuery_person> subQuery = SQLQueryBuilder.subQuery(Desc_subQuery_person.class)
                .select(
                        Mapping.of(desc -> desc.id, tl.id),
                        Mapping.of(desc -> desc.id, Param.of(Long.class, "sdfklsf"))
                )
                .from(tl)
                .where(Condition.Equal.of(tl.id, Param.staticOf(1)))
                .build();

        // 子查询得到一个虚拟的表对象
        Desc_subQuery_person subP = new Desc_subQuery_person(subQuery);

        // 真实的表对象
        Desc_person tp = new Desc_person();

        // 用虚拟的表对象和真实的表对象一起构建一个查询
        SQLQueryMetaDesc<PersonDO> build = SQLQueryBuilder.mappingTo(PersonDO.class)
                .select(
                        Mapping.ofAutoConvert(PersonDO::setId, tp.name),
                        Mapping.of(PersonDO::setName, tp.name),
                        Mapping.of(PersonDO::setId, subP.id)
                ).from(tp)
                .leftJoin(subP, tp.id.eq(subP.id))
                .rightJoin(subP, subP.id.eq(tp.id))
                .where(
                        tp.id.in(Param.of(List.class, "234"))
                ).build();
    }
}
