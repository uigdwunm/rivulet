package vkllyr.jpaminus.describer.query.desc;

/**
 * Description 需要通过反射获取的参数，只需要名称和类型就足够了
 *
 * @author zhaolaiyuan
 * Date 2021/9/20 11:53
 **/
public class Param<T> {

    private final Class<T> clazz;

    private final String paramName;

    private Param(Class<T> clazz, String paramName) {
        this.clazz = clazz;
        this.paramName = paramName;
    }

    public static <T> Param<T> of(Class<T> clazz, String paramName) {
        return new Param<>(clazz, paramName);
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public String getParamName() {
        return paramName;
    }
}
