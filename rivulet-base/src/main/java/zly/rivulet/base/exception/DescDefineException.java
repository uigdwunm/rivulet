package zly.rivulet.base.exception;

import zly.rivulet.base.describer.WholeDesc;

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

    public static DescDefineException noMatchDescKey() {
        return new DescDefineException("没找到匹配的key");
    }

    public static DescDefineException moreSubQuery(int subQueryMax) {
        return new DescDefineException("子查询数量过多，请检查是否存在死循环嵌套子查询," + subQueryMax);
    }
}
