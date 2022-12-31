package zly.rivulet.base.exception;

import zly.rivulet.base.definer.ModelMeta;

import java.lang.reflect.Method;

public class ParseException extends RuntimeException {

    private ParseException(String msg) {
        super(msg);
    }

    private ParseException(String msg, Throwable e) {
        super(msg, e);
    }

    public static ParseException noBindingDesc(Method proxyMethod) {
        return new ParseException("没有绑定desc语句,method=" + proxyMethod.getName());
    }
    public static ParseException noAvailablePrimaryKey(ModelMeta modelMeta) {
        return new ParseException("有且仅有一个主键时，才能以模型方式解析调用," + modelMeta.getModelClass().getName() + "对应的模型无法解析。");
    }
}
