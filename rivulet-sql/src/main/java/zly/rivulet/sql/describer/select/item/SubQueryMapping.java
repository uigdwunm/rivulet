package zly.rivulet.sql.describer.select.item;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.definer.meta.SQLColumnMeta;

import java.util.function.Function;

public class SubQueryMapping<R> implements Mapping<R> {

    private final Function<R, SQLColumnMeta<?>> mappingField;

    private final SingleValueElementDesc<?> singleValueElementDesc;

    /**
     * 指定的转换器，如果为空，则会从默认的转换器中选一个
     **/
    private final Convertor<?, ?> convertor;

    SubQueryMapping(Function mappingField, SingleValueElementDesc<?> singleValueElementDesc, Convertor<?, ?> convertor) {
        this.mappingField = mappingField;
        this.singleValueElementDesc = singleValueElementDesc;
        this.convertor = convertor;
    }

    public Function<R, SQLColumnMeta<?>> getMappingField() {
        return mappingField;
    }

    @Override
    public SingleValueElementDesc<?> getSingleValueElementDesc() {
        return singleValueElementDesc;
    }

    @Override
    public Convertor<?, ?> getConvertor() {
        return convertor;
    }
}
