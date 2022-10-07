package zly.rivulet.sql.exception;

/**
 * Description model模型定义异常
 *
 * @author zhaolaiyuan
 * Date 2022/5/21 11:29
 **/
public class SQLModelDefineException extends RuntimeException {

    public SQLModelDefineException(String msg) {
        super(msg);
    }

    public static SQLModelDefineException zerofillMustUnSigned() {
        return new SQLModelDefineException("零填充属性必须是无符号的!");
    }

    public static SQLModelDefineException needNoArgumentsConstructor() {
        return new SQLModelDefineException("必须有一个无参构造方法!");
    }

    public static SQLModelDefineException noField() {
        return new SQLModelDefineException("未定义对应的字段");
    }

    public static SQLModelDefineException notTable() {
        return new SQLModelDefineException("定义的表对象非法");
    }
}
