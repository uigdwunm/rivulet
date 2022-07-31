package zly.rivulet.mysql.convertor;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.definer.outerType.OriginOuterType;

/**
 * Description 自带转换器的，无需从manager中获取
 *
 * @author zhaolaiyuan
 * Date 2022/7/30 12:36
 **/
public abstract class SelfConvertor<T, O extends OriginOuterType> {

    private final Convertor<T, O> convertor;

    protected SelfConvertor(Convertor<T, O> convertor) {
        this.convertor = convertor;
    }

    public Convertor<T, O> getConvertor() {
        return convertor;
    }
}
