package zly.rivulet.sql.definer.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SQLQueryAlias {

    String value();

    boolean isForce() default true;
}
