package zly.rivulet.mysql.definer.annotations.type.string;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.mysql.convertor.SelfConvertor;

public class JsonDO<T> extends SelfConvertor<T, MySQLJson.Type> {

    private final T data;

    private JsonDO(T data, JsonConvertor<T> jsonConvertor) {
        super(jsonConvertor);
        this.data = data;
    }

    public static <T> JsonDO<T> create(T data, JsonConvertor<T> jsonConvertor) {
        return new JsonDO<>(data, jsonConvertor);
    }

    public T getData() {
        return data;
    }

    abstract static class JsonConvertor<T> extends Convertor<T, MySQLJson.Type> {

        public JsonConvertor(Class<T> javaType, Class<MySQLJson.Type> originOuterType) {
            super(javaType, originOuterType);
        }

        @Override
        public T convertToJavaType(Object outerValue) {
            return toDO((String) outerValue);
        }

        @Override
        public String convertToStatement(T innerValue) {
            return toJson(innerValue);
        }

        public abstract T toDO(String json);


        public abstract String toJson(T t);
    }
}
