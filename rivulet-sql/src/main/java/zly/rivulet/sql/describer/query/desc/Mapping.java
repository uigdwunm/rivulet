package zly.rivulet.sql.describer.query.desc;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.describer.query.SQLQueryMetaDesc;

public class Mapping<F, S, C> {

    /**
     * 里面是一个set方法的lambda表达式
     **/
    private final SetMapping<S, C> mappingField;

    private final SingleValueElementDesc<F, C> desc;

    private Mapping(SetMapping<S, C> mappingField, SingleValueElementDesc<F, C> desc) {
        this.desc = desc;
        this.mappingField = mappingField;
    }

    /**
     * Description 需要转换模型的
     *
     * @author zhaolaiyuan
     * Date 2022/1/3 11:37
     **/
    public static <F, S, C> Mapping<F, S, C> of(SetMapping<S, C> selectField, SQLFunction<F, C> desc) {
        return new Mapping<>(selectField, desc);
    }

    public static <F, S, C> Mapping<F, S, C> of(SetMapping<S, C> selectField, Param<C> desc) {
        return new Mapping<>(selectField, (SingleValueElementDesc) desc);
    }

    public static <F, S, C> Mapping<F, S, C> of(SetMapping<S, C> selectField, FieldMapping<F, C> desc) {
        return new Mapping<>(selectField, desc);
    }

    public static <F, S, C> Mapping<F, S, C> of(SetMapping<S, C> selectField, SQLQueryMetaDesc<F, C> desc) {
        return new Mapping<>(selectField, desc);
    }

    public SetMapping<S, C> getMappingField() {
        return mappingField;
    }

    public SingleValueElementDesc<F, C> getDesc() {
        return desc;
    }

}
