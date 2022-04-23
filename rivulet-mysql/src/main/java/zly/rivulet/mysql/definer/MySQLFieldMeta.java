package zly.rivulet.mysql.definer;

import zly.rivulet.sql.definer.meta.SQLFieldMeta;

public class MySQLFieldMeta implements SQLFieldMeta {

    private String fieldName;

    private Class<?> fieldType;


    private String name;

    private MySQLType type;
}
