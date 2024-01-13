package zly.rivulet.sql.describer.select.item;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ResultConvertor;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.definer.meta.SQLColumnMeta;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.describer.query_.SQLQueryMetaDesc;

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

    /**
     * 函数类的select项映射到vo
     **/
    public static <V, T> CommonMapping<V> of(SetMapping<V, T> selectField, SQLFunction<T> desc) {
        return new CommonMapping<>(selectField, desc, ResultConvertor.SELF_CONVERTOR);
    }
    public static <V, O, T> CommonMapping<V> ofAutoConvert(SetMapping<V, T> selectField, SQLFunction<O> desc) {
        return new CommonMapping<>(selectField, desc, null);
    }
    public static <V, O, T> CommonMapping<V> of(SetMapping<V, T> selectField, SQLFunction<O> desc, Convertor<O, T> convertor) {
        return new CommonMapping<>(selectField, desc, convertor);
    }

    /**
     * 参数类的select项映射到vo
     **/
    public static <V, T> CommonMapping<V> of(SetMapping<V, T> selectField, Param<T> param) {
        return new CommonMapping<>(selectField, param, ResultConvertor.SELF_CONVERTOR);
    }
    public static <V, O, T> CommonMapping<V> ofAutoConvert(SetMapping<V, T> selectField, Param<O> param) {
        return new CommonMapping<>(selectField, param, null);
    }
    public static <V, O, T> CommonMapping<V> of(SetMapping<V, T> selectField, Param<O> param, Convertor<O, T> convertor) {
        return new CommonMapping<>(selectField, param, convertor);
    }

    /**
     * 表字段类的select项映射到vo
     **/
    public static <V, T> CommonMapping<V> of(SetMapping<V, T> selectField, SQLColumnMeta<T> sqlColumnMeta) {
        return new CommonMapping<>(selectField, sqlColumnMeta, ResultConvertor.SELF_CONVERTOR);
    }
    public static <V, O, T> CommonMapping<V> ofAutoConvert(SetMapping<V, T> selectField, SQLColumnMeta<O> sqlColumnMeta) {
        return new CommonMapping<>(selectField, sqlColumnMeta, null);
    }
    public static <V, O, T> CommonMapping<V> of(SetMapping<V, T> selectField, SQLColumnMeta<O> sqlColumnMeta, Convertor<O, T> convertor) {
        return new CommonMapping<>(selectField, sqlColumnMeta, convertor);
    }

    /**
     * 子查询类的select项映射到vo
     **/
    public static <V, T> CommonMapping<V> of(SetMapping<V, T> selectField, SQLQueryMetaDesc<T> sqlQueryMetaDesc) {
        return new CommonMapping<>(selectField, sqlQueryMetaDesc, ResultConvertor.SELF_CONVERTOR);
    }
    public static <V, O, T> CommonMapping<V> ofAutoConvert(SetMapping<V, T> selectField, SQLQueryMetaDesc<O> sqlQueryMetaDesc) {
        return new CommonMapping<>(selectField, sqlQueryMetaDesc, null);
    }
    public static <V, O, T> CommonMapping<V> of(SetMapping<V, T> selectField, SQLQueryMetaDesc<O> sqlQueryMetaDesc, Convertor<O, T> convertor) {
        return new CommonMapping<>(selectField, sqlQueryMetaDesc, convertor);
    }
}
