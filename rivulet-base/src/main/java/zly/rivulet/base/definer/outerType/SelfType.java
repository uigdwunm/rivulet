package zly.rivulet.base.definer.outerType;

/**
 * Description 当参数转换成语句时，没有外部类型对应，所以这里只需要转换成默认类型即可，
 * 也就是说，这里都只会向外转换而不会向内
 *
 * @author zhaolaiyuan
 * Date 2022/6/3 9:25
 **/
public class SelfType implements OriginOuterType {
    @Override
    public Class<?> getOuterType() {
        return null;
    }
}
