package zly.rivulet.mysql.desc;

import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.ParamCheckType;
import zly.rivulet.mysql.discriber.meta.MySQLColumnMeta;
import zly.rivulet.mysql.discriber.meta.MySQLTableMeta;
import zly.rivulet.mysql.model.PersonDO;
import zly.rivulet.sql.definer.meta.SQLColumnMeta;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.query_.SQLQueryMetaDesc;
import zly.rivulet.sql.describer.select.JoinByBuilder;
import zly.rivulet.sql.describer.select.SQLQueryBuilder;
import zly.rivulet.sql.describer.select.SelectByBuilder;
import zly.rivulet.sql.describer.select.WhereByBuilder;
import zly.rivulet.sql.describer.select.item.CommonMapping;
import zly.rivulet.sql.describer.select.item.Mapping;
import zly.rivulet.sql.describer.update.WhereBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Desc_person extends MySQLTableMeta {
    public static final String TABLE_NAME = "t_person";
    public static final Desc_person THIS = new Desc_person();

    public final MySQLColumnMeta<Long> id = new MySQLColumnMeta<>("id", this, Long.class);

    public final MySQLColumnMeta<String> name = new MySQLColumnMeta<>("name", this, String.class);

    @Override
    public List<SQLColumnMeta<?>> primaryKey() {
        return Collections.singletonList(id);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String name() {
        return TABLE_NAME;
    }

    public static SQLQueryMetaDesc<PersonDO> quickQueryByIdList(Collection<Long> idList) {
        Function<Desc_person, Condition> columnMeta = desc_person -> {
            return desc_person.id.in(Param.staticOf(idList, ParamCheckType.PLACEHOLDER));
        };
        return quickQueryBy(columnMeta);
    }

    public static SQLQueryMetaDesc<PersonDO> quickQueryById(Long id) {
        Function<Desc_person, Condition> columnMeta = desc_person -> {
            return desc_person.id.eq(Param.staticOf(id, ParamCheckType.PLACEHOLDER));
        };
        return quickQueryBy(columnMeta);
    }

    public static SQLQueryMetaDesc<PersonDO> quickQueryBy(Function<Desc_person, Condition> columnMeta) {
        Condition condition = columnMeta.apply(THIS);
        return quickQueryBy(condition);
    }

    /**
     * 单表查询快捷构建
     **/
    public static SQLQueryMetaDesc<PersonDO> quickQueryBy(Condition ... conditions) {
        return THIS.queryBuilder()
                .where(conditions)
                .build();
    }

    /**
     * 单表查询快捷构建
     **/
    public WhereByBuilder<PersonDO> queryBuilder() {

//        SelectByBuilder<PersonDO> selectBuilder = new SelectByBuilder<>();
////        selectBuilder.resultModelClass = clazz;
//        return selectBuilder;
        return SQLQueryBuilder.mappingTo(PersonDO.class)
                .select(
                        CommonMapping.of(PersonDO::setId, id),
                        CommonMapping.of(PersonDO::setName, name)
                )
                .from(this);
    }
}
