package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.query.SQLQueryMetaDesc;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface UnaryOperation {

    BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCollect();

    default <F, C> SQLFunction<F, C> of(FieldMapping<F, C> value) {
        return new SQLFunction<F, C>(Collections.singletonList(value), targetType) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(JoinFieldMapping<C> value) {
        return new SQLFunction<F, C>(Collections.singletonList((SingleValueElementDesc) value), targetType) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(Function<F, C> value) {
        return new SQLFunction<F, C>(Collections.singletonList((SingleValueElementDesc) value), targetType) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(Param<C> value) {
        return new SQLFunction<F, C>(Collections.singletonList((SingleValueElementDesc) value), targetType) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    default <F, C> SQLFunction<F, C> of(SQLQueryMetaDesc<F, C> value) {
        return new SQLFunction<F, C>(Collections.singletonList(value), targetType) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }
}
