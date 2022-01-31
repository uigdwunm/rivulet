package vkllyr.jpaminus.exception;

/**
 * Description 理论上不可能出现的异常
 *
 * @author zhaolaiyuan
 * Date 2021/11/6 10:19
 **/
public class UnbelievableException extends RuntimeException {

    public UnbelievableException(String msg) {
        super(msg);
    }

    public UnbelievableException(String msg, Throwable e) {
        super(msg, e);
    }

    public static UnbelievableException illegalAccess(Throwable e) {
        return new UnbelievableException("无权限访问（不应该出现，已经处理过校验）", e);
    }
}
