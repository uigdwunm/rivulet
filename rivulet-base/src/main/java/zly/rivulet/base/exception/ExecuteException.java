package zly.rivulet.base.exception;

import zly.rivulet.base.definition.param.ParamReceipt;

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
}
