package zly.rivulet.sql.definer.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SQLTable {

    /**
     * Description 表名，必填
     *
     * @author zhaolaiyuan
     * Date 2022/5/1 13:57
     **/
    String value();
}
