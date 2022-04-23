package zly.rivulet.sql.definer.annotations;

public @interface SqlColumn {

    String columnName() default "";

    String value() default "";
}
