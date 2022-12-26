package zly.rivulet.base.exception;

public class ExecuteException extends RuntimeException {

    private ExecuteException(String msg) {
        super(msg);
    }

    private ExecuteException(String msg, Throwable e) {
        super(msg, e);
    }


    public static ExecuteException execError(String s) {
        return new ExecuteException(s);
    }

    public static ExecuteException rivuletIsClosed() {
        return new ExecuteException("当前连接已经关闭");
    }
}
