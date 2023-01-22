package zly.rivulet.mysql.discriber.function.cast;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.JoinFieldMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class CastOperation<C> {

    private final String castType;

    public CastOperation(String castType) {
        this.castType = castType;
    }

    public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCollect() {
        return (customCollector, customSingleValueWraps) -> {
            customCollector.append("CAST").append("(");
            customCollector.append(customSingleValueWraps.get(0)).space().append("AS").space().append(castType);
            customCollector.append(")");
        };
    }

    public <F, O> SQLFunction<F, C> of(FieldMapping<F, O> value) {
        return new SQLFunction<F, C>(Collections.singletonList((SingleValueElementDesc) value)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    public <F, O> SQLFunction<F, C> of(JoinFieldMapping<O> value) {
        return new SQLFunction<F, C>(Collections.singletonList((SingleValueElementDesc) value)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    public <F, O> SQLFunction<F, C> of(SQLFunction<F, O> value) {
        return new SQLFunction<F, C>(Collections.singletonList((SingleValueElementDesc) value)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    public <F, O> SQLFunction<F, C> of(Param<O> value) {
        return new SQLFunction<F, C>(Collections.singletonList((SingleValueElementDesc) value)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }

    public <F, O> SQLFunction<F, C> of(SqlQueryMetaDesc<F, O> value) {
        return new SQLFunction<F, C>(Collections.singletonList((SingleValueElementDesc) value)) {
            @Override
            public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
                return getCollect();
            }
        };
    }
}
