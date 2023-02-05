package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Mapping;

import java.util.Arrays;
import java.util.Collections;

public class SelectByBuilder<F, S> extends WhereByBuilder<F, S> {

    public SelectByBuilder(Class<F> from, Class<S> select) {
        super.modelFrom = from;
        super.selectModel = select;
    }

    @SafeVarargs
    public final WhereByBuilder<F, S> select(Mapping<F, S, ?> ... items) {
        super.mappedItemList = Arrays.asList(items);
        return this;
    }

    public final WhereByBuilder<F, S> selectOne(Param<?> desc) {
        SetMapping<OneResult, ?> mappingField = OneResult::setValue;
        super.mappedItemList = Collections.singletonList(Mapping.of((SetMapping) mappingField, desc));
        super.isOneResult = true;
        return this;
    }

    public final WhereByBuilder<F, S> selectOne(FieldMapping<F, ?> desc) {
        SetMapping<OneResult, ?> mappingField = OneResult::setValue;
        super.mappedItemList = Collections.singletonList(Mapping.of((SetMapping) mappingField, desc));
        super.isOneResult = true;
        return this;
    }

    public final WhereByBuilder<F, S> selectOne(SqlQueryMetaDesc<F, ?> desc) {
        SetMapping<OneResult, ?> mappingField = OneResult::setValue;
        super.mappedItemList = Collections.singletonList(Mapping.of((SetMapping) mappingField, desc));
        super.isOneResult = true;
        return this;
    }

    public final WhereByBuilder<F, S> selectOne(SQLFunction<F, ?> desc) {
        SetMapping<OneResult, ?> mappingField = OneResult::setValue;
        super.mappedItemList = Collections.singletonList(Mapping.of((SetMapping) mappingField, desc));
        super.isOneResult = true;
        return this;
    }

    public static class OneResult<T> {

        private T value;
        private Class<T> valueType;

        public void setValue(T value) {
            this.value = value;
            this.valueType = valueType;
        }

        public T getValue() {
            return value;
        }

        public Class<T> getValueType() {
            return valueType;
        }
    }
}
