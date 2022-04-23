package zly.rivulet.base.exception;

public class ModelDefineException extends RuntimeException {

    public ModelDefineException(String msg) {
        super(msg);
    }

    public static ModelDefineException multiMainFromMode() {
        return new ModelDefineException("连表查询只有有一个查询主体");
    }
}
