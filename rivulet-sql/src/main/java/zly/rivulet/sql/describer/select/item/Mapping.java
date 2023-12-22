package zly.rivulet.sql.describer.select.item;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ResultConvertor;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.definer.meta.SQLColumnMeta;
import zly.rivulet.sql.describer.query_.SQLQueryMetaDesc;

import java.util.function.Function;

public interface Mapping<R> {

    /**
     * 函数类的select项映射到vo
     **/
    static <V, T> CommonMapping<V> of(SetMapping<V, T> selectField, SQLFunction<T> desc) {
        return new CommonMapping<>(selectField, desc, ResultConvertor.SELF_CONVERTOR);
    }
    static <V, O, T> CommonMapping<V> ofAutoConvert(SetMapping<V, T> selectField, SQLFunction<O> desc) {
        return new CommonMapping<>(selectField, desc, null);
    }
    static <V, O, T> CommonMapping<V> of(SetMapping<V, T> selectField, SQLFunction<O> desc, Convertor<O, T> convertor) {
        return new CommonMapping<>(selectField, desc, convertor);
    }

    /**
     * 参数类的select项映射到vo
     **/
    static <V, T> CommonMapping<V> of(SetMapping<V, T> selectField, Param<T> param) {
        return new CommonMapping<>(selectField, param, ResultConvertor.SELF_CONVERTOR);
    }
    static <V, O, T> CommonMapping<V> ofAutoConvert(SetMapping<V, T> selectField, Param<O> param) {
        return new CommonMapping<>(selectField, param, null);
    }
    static <V, O, T> CommonMapping<V> of(SetMapping<V, T> selectField, Param<O> param, Convertor<O, T> convertor) {
        return new CommonMapping<>(selectField, param, convertor);
    }

    /**
     * 表字段类的select项映射到vo
     **/
    static <V, T> CommonMapping<V> of(SetMapping<V, T> selectField, SQLColumnMeta<T> sqlColumnMeta) {
        return new CommonMapping<>(selectField, sqlColumnMeta, ResultConvertor.SELF_CONVERTOR);
    }
    static <V, O, T> CommonMapping<V> ofAutoConvert(SetMapping<V, T> selectField, SQLColumnMeta<O> sqlColumnMeta) {
        return new CommonMapping<>(selectField, sqlColumnMeta, null);
    }
    static <V, O, T> CommonMapping<V> of(SetMapping<V, T> selectField, SQLColumnMeta<O> sqlColumnMeta, Convertor<O, T> convertor) {
        return new CommonMapping<>(selectField, sqlColumnMeta, convertor);
    }

    /**
     * 子查询类的select项映射到vo
     **/
    static <V, T> CommonMapping<V> of(SetMapping<V, T> selectField, SQLQueryMetaDesc<T> sqlQueryMetaDesc) {
        return new CommonMapping<>(selectField, sqlQueryMetaDesc, ResultConvertor.SELF_CONVERTOR);
    }
    static <V, O, T> CommonMapping<V> ofAutoConvert(SetMapping<V, T> selectField, SQLQueryMetaDesc<O> sqlQueryMetaDesc) {
        return new CommonMapping<>(selectField, sqlQueryMetaDesc, null);
    }
    static <V, O, T> CommonMapping<V> of(SetMapping<V, T> selectField, SQLQueryMetaDesc<O> sqlQueryMetaDesc, Convertor<O, T> convertor) {
        return new CommonMapping<>(selectField, sqlQueryMetaDesc, convertor);
    }

    /**
     * 函数类的select项映射到子查询对象
     **/
    static <V, T> SubQueryMapping<V> of(Function<V, SQLColumnMeta<T>> selectField, SQLFunction<T> desc) {
        return new SubQueryMapping<>(selectField, desc, ResultConvertor.SELF_CONVERTOR);
    }
    static <V, O, T> SubQueryMapping<V> ofAutoConvert(Function<V, SQLColumnMeta<T>> selectField, SQLFunction<O> desc) {
        return new SubQueryMapping<>(selectField, desc, null);
    }
    static <V, O, T> SubQueryMapping<V> of(Function<V, SQLColumnMeta<T>> selectField, SQLFunction<O> desc, Convertor<O, T> convertor) {
        return new SubQueryMapping<>(selectField, desc, convertor);
    }

    /**
     * 参数类的select项映射到子查询对象
     **/
    static <V, T> SubQueryMapping<V> of(Function<V, SQLColumnMeta<T>> selectField, Param<T> param) {
        return new SubQueryMapping<>(selectField, param, ResultConvertor.SELF_CONVERTOR);
    }
    static <V, O, T> SubQueryMapping<V> ofAutoConvert(Function<V, SQLColumnMeta<T>> selectField, Param<O> param) {
        return new SubQueryMapping<>(selectField, param, null);
    }
    static <V, O, T> SubQueryMapping<V> of(Function<V, SQLColumnMeta<T>> selectField, Param<O> param, Convertor<O, T> convertor) {
        return new SubQueryMapping<>(selectField, param, convertor);
    }

    /**
     * 表字段类的select项映射到子查询对象
     **/
    static <V, T> SubQueryMapping<V> of(Function<V, SQLColumnMeta<T>> selectField, SQLColumnMeta<T> sqlColumnMeta) {
        return new SubQueryMapping<>(selectField, sqlColumnMeta, ResultConvertor.SELF_CONVERTOR);
    }
    static <V, O, T> SubQueryMapping<V> ofAutoConvert(Function<V, SQLColumnMeta<T>> selectField, SQLColumnMeta<O> sqlColumnMeta) {
        return new SubQueryMapping<>(selectField, sqlColumnMeta, null);
    }
    static <V, O, T> SubQueryMapping<V> of(Function<V, SQLColumnMeta<T>> selectField, SQLColumnMeta<O> sqlColumnMeta, Convertor<O, T> convertor) {
        return new SubQueryMapping<>(selectField, sqlColumnMeta, convertor);
    }

    /**
     * 子查询类的select项映射到子查询对象
     **/
    static <V, T> SubQueryMapping<V> of(Function<V, SQLColumnMeta<T>> selectField, SQLQueryMetaDesc<T> sqlQueryMetaDesc) {
        return new SubQueryMapping<>(selectField, sqlQueryMetaDesc, ResultConvertor.SELF_CONVERTOR);
    }
    static <V, O, T> SubQueryMapping<V> ofAutoConvert(Function<V, SQLColumnMeta<T>> selectField, SQLQueryMetaDesc<O> sqlQueryMetaDesc) {
        return new SubQueryMapping<>(selectField, sqlQueryMetaDesc, null);
    }
    static <V, O, T> SubQueryMapping<V> of(Function<V, SQLColumnMeta<T>> selectField, SQLQueryMetaDesc<O> sqlQueryMetaDesc, Convertor<O, T> convertor) {
        return new SubQueryMapping<>(selectField, sqlQueryMetaDesc, convertor);
    }

    SingleValueElementDesc<?> getSingleValueElementDesc();

    Convertor<?, ?> getConvertor();
}
