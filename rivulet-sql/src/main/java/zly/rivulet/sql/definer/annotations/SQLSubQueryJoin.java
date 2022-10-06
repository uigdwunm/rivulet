package zly.rivulet.sql.definer.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SQLSubQueryJoin {

    /**
     * Description 关联的子查询key
     *
     * @author zhaolaiyuan
     * Date 2022/5/15 11:02
     **/
    String value();
}
