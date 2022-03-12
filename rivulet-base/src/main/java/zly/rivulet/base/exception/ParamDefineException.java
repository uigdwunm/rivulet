package zly.rivulet.base.exception;

public class ParamDefineException extends RuntimeException {

    public ParamDefineException(String msg) {
        super(msg);
    }

    public static ParamDefineException pathTooLong(String paramName) {
        return new ParamDefineException("定义的参数路径过长（不能超过10），paramPath=" + paramName);
    }

    public static ParamDefineException pathIllegal(String paramName) {
        return new ParamDefineException("定义的参数路径无效，paramPath=" + paramName);
    }
}
