package zly.rivulet.base.exception;

/**
 * Description 理论上不可能出现的异常
 *
 * @author zhaolaiyuan
 * Date 2021/11/6 10:19
 **/
public class UnbelievableException extends RuntimeException {

    private UnbelievableException(String msg) {
        super(msg);
    }

    private UnbelievableException(String msg, Throwable e) {
        super(msg, e);
    }

    public static UnbelievableException unbelievable() {
        return new UnbelievableException("不可能出现的异常");
    }

    public static UnbelievableException unknownType() {
        return new UnbelievableException("未知的类型");
    }

    public static UnbelievableException illegalAccess(Throwable e) {
        return new UnbelievableException("无权限访问（不应该出现，已经处理过校验）", e);
    }

    public static UnbelievableException cloneNotSupportedException(Throwable e) {
        return new UnbelievableException("不支持克隆方法（不应该出现，都已经实现过）", e);
    }

    public static UnbelievableException unNeedSort() {
        return new UnbelievableException("这里应该永远不需要排序");
    }

    public static UnbelievableException nativeClone() {
        return new UnbelievableException("调用原生的clone方法");
    }

}
