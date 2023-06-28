package zly.rivulet.sql.describer.select.item;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.meta.SQLColumnMeta;
import zly.rivulet.sql.describer.query.SQLQueryMetaDesc;

public class Mapping<R> {

    /**
     * 里面是一个set方法的lambda表达式
     **/
    private final SetMapping<R, ?> mappingField;
    
    private final SingleValueElementDesc singleValueElementDesc;

    private Mapping(SetMapping<R, ?> mappingField, SingleValueElementDesc singleValueElementDesc) {
        this.mappingField = mappingField;
        this.singleValueElementDesc = singleValueElementDesc;
    }

    /**
     * Description 需要转换模型的
     *
     * @author zhaolaiyuan
     * Date 2022/1/3 11:37
     **/
//    public static <R> Mapping<R> of(SetMapping<R, ?> selectField, SQLFunction<F, C> desc) {
//        return new Mapping<>(selectField, desc);
//    }

    public static <R> Mapping<R> of(SetMapping<R, ?> selectField, Param<?> param) {
        return new Mapping<>(selectField, param);
    }

    public static <R, C> Mapping<R> of(SetMapping<R, C> selectField, SQLColumnMeta sqlColumnMeta) {
        return new Mapping<>(selectField, sqlColumnMeta);
    }

    public static <R> Mapping<R> of(SetMapping<R, ?> selectField, SQLQueryMetaDesc<?> sqlQueryMetaDesc) {
        return new Mapping<>(selectField, sqlQueryMetaDesc);
    }

    public SetMapping<R, ?> getMappingField() {
        return mappingField;
    }

    public SingleValueElementDesc getSingleValueElementDesc() {
        return singleValueElementDesc;
    }
}
