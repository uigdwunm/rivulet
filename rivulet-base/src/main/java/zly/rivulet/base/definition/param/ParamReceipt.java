package zly.rivulet.base.definition.param;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;

/**
 * Description param凭证，凭证指不是真正的参数，执行时可以通过凭证换取真正的参数
 *
 * @author zhaolaiyuan
 * Date 2022/10/2 11:46
 **/
public interface ParamReceipt extends SingleValueElementDefinition {

    Class<?> getType();

    Convertor<?, ?> getConvertor();
}
