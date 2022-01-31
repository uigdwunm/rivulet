package pers.zly.sql.discriber.query.desc;

import pers.zly.base.describer.SingleValueElementDesc;
import pers.zly.base.describer.field.FieldMapping;
import pers.zly.base.describer.field.SelectMapping;
import pers.zly.sql.discriber.function.MFunctionDesc;

public class Mapping {

    /**
     * Description 需要转换模型的
     *
     * @author zhaolaiyuan
     * Date 2022/1/3 11:37
     **/
    public static <F, S, C> Item<F, S, C> of(SelectMapping<S, C> selectField, MFunctionDesc<F, C> desc) {
        return new Item<>(selectField, desc);
    }

    public static <F, S, C> Item<F, S, C> of(SelectMapping<S, C> selectField, FieldMapping<F, C> desc) {
        return new Item<>(selectField, desc);
    }

    public static class Item<F, S, C> {

        private final SelectMapping<S, C> selectField;

        /**
         * 里面是一个set方法的lambda表达式
         **/
        private final SingleValueElementDesc<F, C> desc;

        private Item(SelectMapping<S, C> selectField, SingleValueElementDesc<F, C> desc) {
            this.desc = desc;
            this.selectField = selectField;
        }
    }
}
