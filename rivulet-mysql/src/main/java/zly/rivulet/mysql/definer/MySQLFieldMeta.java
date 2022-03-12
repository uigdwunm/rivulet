package zly.rivulet.mysql.definer;

import zly.rivulet.base.definer.FieldMeta;

public class MySQLFieldMeta implements FieldMeta {

    private String fieldName;

    private Class<?> fieldType;


    private String name;

    private MySQLType type;
}
