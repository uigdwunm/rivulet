package zly.rivulet.sql.describer.select.item;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ResultConvertor;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.describer.meta.SQLColumnMeta;
import zly.rivulet.sql.describer.query.SQLQueryMetaDesc;

import java.util.function.Function;

public class Mapping<R> {

    /**
     * 里面是一个set方法的lambda表达式
     **/
    private final SetMapping<R, ?> mappingField;
    
    private final SingleValueElementDesc<?> singleValueElementDesc;

    /**
     * 指定的转换器，如果为空，则会从默认的转换器中选一个
     **/
    private final Convertor<?, ?> convertor;

    private Mapping(SetMapping<R, ?> mappingField, SingleValueElementDesc<?> singleValueElementDesc, Convertor<?, ?> convertor) {
        this.mappingField = mappingField;
        this.singleValueElementDesc = singleValueElementDesc;
        this.convertor = convertor;
    }

    public static <V, O, T> Mapping<V> of(SetMapping<V, T> selectField, SQLFunction<?, O> desc) {
        return of(selectField, desc, null);
    }

    public static <V, O, T> Mapping<V> of(SetMapping<V, T> selectField, SQLFunction<?, O> desc, Convertor<O, T> convertor) {
        return new Mapping<>(selectField, desc, convertor);
    }

    public static <V, O, T> Mapping<V> of(SetMapping<V, T> selectField, Param<O> param) {
        return of(selectField, param, null);
    }

    public static <V, O, T> Mapping<V> of(SetMapping<V, T> selectField, Param<O> param, Convertor<O, T> convertor) {
        return new Mapping<>(selectField, param, convertor);
    }

    public static <V, O, T> Mapping<V> of(SetMapping<V, T> selectField, SQLColumnMeta<O> sqlColumnMeta) {
        return of(selectField, sqlColumnMeta, null);
    }

    public static <V, O, T> Mapping<V> of(SetMapping<V, T> selectField, SQLColumnMeta<O> sqlColumnMeta, Convertor<O, T> convertor) {
        return new Mapping<>(selectField, sqlColumnMeta, convertor);
    }

    public static <V, O, T> Mapping<V> of(SetMapping<V, T> selectField, SQLQueryMetaDesc<O> sqlQueryMetaDesc) {
        return of(selectField, sqlQueryMetaDesc, null);
    }

    public static <V, O, T> Mapping<V> of(SetMapping<V, T> selectField, SQLQueryMetaDesc<O> sqlQueryMetaDesc, Convertor<O, T> convertor) {
        return new Mapping<>(selectField, sqlQueryMetaDesc, convertor);
    }

    public SetMapping<R, ?> getMappingField() {
        return mappingField;
    }

    public SingleValueElementDesc<?> getSingleValueElementDesc() {
        return singleValueElementDesc;
    }

    public Convertor<?, ?> getConvertor() {
        return convertor;
    }
}
