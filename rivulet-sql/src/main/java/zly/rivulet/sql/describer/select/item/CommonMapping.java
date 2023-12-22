package zly.rivulet.sql.describer.select.item;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.SetMapping;

public class CommonMapping<R> implements Mapping<R> {

    /**
     * 里面是一个set方法的lambda表达式
     **/
    private final SetMapping<R, ?> mappingField;

    private final SingleValueElementDesc<?> singleValueElementDesc;

    /**
     * 指定的转换器，如果为空，则会从默认的转换器中选一个
     **/
    private final Convertor<?, ?> convertor;

    CommonMapping(SetMapping<R, ?> mappingField, SingleValueElementDesc<?> singleValueElementDesc, Convertor<?, ?> convertor) {
        this.mappingField = mappingField;
        this.singleValueElementDesc = singleValueElementDesc;
        this.convertor = convertor;
    }

    public SetMapping<R, ?> getMappingField() {
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
