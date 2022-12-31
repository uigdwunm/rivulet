package zly.rivulet.base.exception;

public class DescDefineException extends RuntimeException {

    public DescDefineException(String msg) {
        super(msg);
    }

    public static DescDefineException noMatchDescKey(String key) {
        return new DescDefineException("未找到key为'" + key + "'的desc配置");
    }
}
