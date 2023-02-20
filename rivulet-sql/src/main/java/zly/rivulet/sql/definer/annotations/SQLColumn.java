package zly.rivulet.sql.definer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SQLColumn {

    /**
     * Description 字段名，默认是变量名
     *
     * @author zhaolaiyuan
     * Date 2022/5/1 13:56
     **/
    String value() default "";
}
