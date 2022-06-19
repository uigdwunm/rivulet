package zly.rivulet.sql.describer.query.desc;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.SelectMapping;
import zly.rivulet.sql.describer.function.MFunctionDesc;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;

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

    public static <F, S, C> Item<F, S, C> of(SelectMapping<S, C> selectField, SqlQueryMetaDesc<F, C> desc) {
        return new Item<>(selectField, desc);
    }


    public static class Item<F, S, C> {

        /**
         * 里面是一个set方法的lambda表达式
         **/
        private final SelectMapping<S, C> selectField;

        private final SingleValueElementDesc<F, C> desc;

        private Item(SelectMapping<S, C> selectField, SingleValueElementDesc<F, C> desc) {
            this.desc = desc;
            this.selectField = selectField;
        }

        public SelectMapping<S, C> getSelectField() {
            return selectField;
        }

        public SingleValueElementDesc<F, C> getDesc() {
            return desc;
        }
    }
}
