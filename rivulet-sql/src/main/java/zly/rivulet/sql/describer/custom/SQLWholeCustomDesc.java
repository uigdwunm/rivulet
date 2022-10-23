package zly.rivulet.sql.describer.custom;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomDesc;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Description 整个语句的自定义语句，既然要自定义，就全都自己写，参数啥的都自己拼上
 *
 * @author zhaolaiyuan
 * Date 2022/10/23 13:41
 **/
public class SQLWholeCustomDesc implements CustomDesc {

    private final Consumer<CustomCollector> consumer;

    public SQLWholeCustomDesc(Consumer<CustomCollector> consumer) {
        this.consumer = consumer;
    }

    @Override
    public List<SingleValueElementDesc<?, ?>> getSingleValueList() {
        return Collections.emptyList();
    }

    @Override
    public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
        return ((customCollector, customSingleValueWraps) -> consumer.accept(customCollector));
    }
}
