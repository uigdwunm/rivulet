package zly.rivulet.base.exception;

public class DescDefineException extends RuntimeException {

    public DescDefineException(String msg) {
        super(msg);
    }

    public static DescDefineException unknownFromModel(Class<?> knownModel) {
        if (knownModel == null) {
            return new DescDefineException("查询模型不能为空");
        }
        return new DescDefineException("未知的查询模型" + knownModel.getName());
    }
}
