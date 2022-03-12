package zly.rivulet.mysql.exception;

public class ModelDefineException extends RuntimeException {

    public ModelDefineException(String msg) {
        super(msg);
    }

    public static ModelDefineException zerofillMustUnSigned() {
        return new ModelDefineException("零填充属性必须是无符号的!");
    }
}
