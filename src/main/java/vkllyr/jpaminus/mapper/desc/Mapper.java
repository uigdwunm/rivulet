package vkllyr.jpaminus.mapper.desc;

import java.util.function.Function;

public class Mapper<O, T, R> {

    private final Function<T, R> mapped;

    private final Function<O, R> origin;

    public Mapper(Function<T, R> mapped, Function<O, R> origin) {
        this.mapped = mapped;
        this.origin = origin;
    }

    /**
     * Description 映射一个字段，表字段映射到目标模型字段
     *
     * @author zhaolaiyuan
     * Date 2021/11/27 13:04
     **/
    public static <O, T, R> Mapper<O, T, R> map(Function<T, R> mapped, Function<O, R> origin) {
        return new Mapper<>(mapped, origin);
    }

}
