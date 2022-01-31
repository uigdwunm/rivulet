package pers.zly.sql.discriber.query.desc;

import pers.zly.base.describer.SingleValueElementDesc;

public class Where {


    public static <F, C> Item<F, C> equalTo(SingleValueElementDesc<F, C> fieldMapped1, SingleValueElementDesc<F, C> fieldMapped2) {
        return new Item<>(fieldMapped1, fieldMapped2);
    }

    public static class Item<F, C> {
        private final SingleValueElementDesc<F, C> fieldMapped1;
        private final SingleValueElementDesc<F, C> fieldMapped2;

        public Item(SingleValueElementDesc<F, C> fieldMapped1, SingleValueElementDesc<F, C> fieldMapped2) {
            this.fieldMapped1 = fieldMapped1;
            this.fieldMapped2 = fieldMapped2;
        }
    }
}
