package zly.rivulet.mysql.discriber.function.cast;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;
import zly.rivulet.sql.describer.function.Function;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class Cast<F, C> implements Function<F, C> {

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

        private Type(String type) {
            this.type = type;
        }

        public Type binary() {
            return new Type("BINARY");
        }

        public Type binary(int length) {
            return new Type("BINARY(" + length + ")");
        }

        public Type charType(int length) {
            return new Type("CHAR(" + length + ")");
        }

        public Type date() {
            return new Type("DATE");
        }

        public Type datetime() {
            return new Type("DATETIME");
        }

        public Type decimal(int a, int b) {
            return new Type("decimal(" + a + "," + b + ")");
        }

        public Type nchar(int length) {
            return new Type("NCHAR(" + length + ")");
        }

        public Type signed(int length) {
            return new Type("SIGNED(" + length + ")");
        }

        public Type time() {
            return new Type("TIME");
        }

        public Type unsigned(int length) {
            return new Type("UNSIGNED(" + length + ")");
        }
    }
}
