package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.query.SQLQueryMetaDesc;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public interface BinaryOperation {

    BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCollect();

    default <F, C> SQLFunction<F, C> of(FieldMapping<F, C> leftValue, FieldMapping<F, C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList(leftValue, rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(FieldMapping<F, C> leftValue, SQLFunction<F, C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList(leftValue, rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(FieldMapping<F, C> leftValue, Param<C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(FieldMapping<F, C> leftValue, SQLQueryMetaDesc<F, C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(JoinFieldMapping<C> leftValue, JoinFieldMapping<C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(JoinFieldMapping<C> leftValue, SQLFunction<F, C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(JoinFieldMapping<C> leftValue, Param<C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(JoinFieldMapping<C> leftValue, SQLQueryMetaDesc<F, C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(SQLFunction<F, C> leftValue, FieldMapping<F, C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(SQLFunction<F, C> leftValue, JoinFieldMapping<C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(SQLFunction<F, C> leftValue, SQLFunction<F, C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(SQLFunction<F, C> leftValue, Param<C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(SQLFunction<F, C> leftValue, SQLQueryMetaDesc<F, C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(Param<C> leftValue, FieldMapping<F, C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(Param<C> leftValue, JoinFieldMapping<C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(Param<C> leftValue, SQLFunction<F, C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(Param<C> leftValue, Param<C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(Param<C> leftValue, SQLQueryMetaDesc<F, C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(SQLQueryMetaDesc<F, C> leftValue, FieldMapping<F, C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(SQLQueryMetaDesc<F, C> leftValue, JoinFieldMapping<C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(SQLQueryMetaDesc<F, C> leftValue, SQLFunction<F, C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(SQLQueryMetaDesc<F, C> leftValue, Param<C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(SQLQueryMetaDesc<F, C> leftValue, SQLQueryMetaDesc<F, C> rightValue) {
        return new SQLFunction<F, C>(Arrays.asList((SingleValueElementDesc) leftValue, (SingleValueElementDesc) rightValue)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }
}
