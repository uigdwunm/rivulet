package zly.rivulet.mysql.discriber.function.cast;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;
import zly.rivulet.sql.describer.function.SQLFunction;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class Cast<F, C> implements SQLFunction<F, C> {

    private final SingleValueElementDesc<F, C> value;

    private final Type type;

    public Cast(SingleValueElementDesc<F, C> value, Type type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public List<SingleValueElementDesc<?, ?>> getSingleValueList() {
        return Collections.singletonList(value);
    }

    @Override
    public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
        return (customCollector, customSingleValueWraps) -> {
            customCollector.append("CAST").append("(");
            customCollector.append(customSingleValueWraps.get(0)).space().append("AS").space().append(type.type);
            customCollector.append(")");
        };
    }

    public static class Type {
        private final String type;

        public Type(String type) {
            this.type = type;
        }

        public Type binary() {
            return new Type("BINARY");
        }

        public Type binary(int length) {
            return new Type("BINARY(" + length + ")");
        }

        BINARY(),
        CHAR(),
        DATE,
        DATETIME,
        DECIMAL(, ),
        NCHAR(),
        SINGED(),
        TIME,
        UNSIGNED();
    }
}
