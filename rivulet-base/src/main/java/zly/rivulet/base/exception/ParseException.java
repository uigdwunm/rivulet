package zly.rivulet.base.exception;

public class ParseException extends RuntimeException {

    private ParseException(String msg) {
        super(msg);
    }

    private ParseException(String msg, Throwable e) {
        super(msg, e);
    }

    public static ParseException undefinedMethod() {
        return new ParseException("无法执行未定义的方法");
    }

    public static ParseException noConstructor(Throwable e) {
        return new ParseException("没有预定义好指定的构造方法", e);
    }

    public static ParseException newStatementFail(Throwable e) {
        return new ParseException("实例化statement失败", e);
    }
}
