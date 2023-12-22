package zly.rivulet.sql.definer.meta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface SQLQueryMeta {

    String name();

    default List<SQLColumnMeta<?>> getAllColumnMeta() {
        Class<? extends SQLQueryMeta> c = this.getClass();
        return Arrays.stream(c.getDeclaredFields())
                .filter(field -> field.getDeclaringClass().isAssignableFrom(SQLColumnMeta.class))
                .map(field -> {
                    try {
                        return (SQLColumnMeta<?>) field.get(this);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }
}
