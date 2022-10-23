package zly.rivulet.sql.describer.custom;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomDesc;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;
import zly.rivulet.base.describer.field.FieldMapping;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Description 代替部分语句的custom语句，可以解析FieldMapping和Param的singleValue
 *
 * @author zhaolaiyuan
 * Date 2022/10/23 13:37
 **/
public class SQLPartCustomDesc implements CustomDesc {

    private final List<FieldMapping<?, ?>> fieldMappingList;

    private final BiConsumer<CustomCollector, List<CustomSingleValueWrap>> biConsumer;

    public SQLPartCustomDesc(List<FieldMapping<?, ?>> fieldMappingList, BiConsumer<CustomCollector, List<CustomSingleValueWrap>> biConsumer) {
        this.fieldMappingList = fieldMappingList;
        this.biConsumer = biConsumer;
    }

    @Override
    public List<SingleValueElementDesc<?, ?>> getSingleValueList() {
        return (List) this.fieldMappingList;
    }

    @Override
    public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
        return biConsumer;
    }
}
