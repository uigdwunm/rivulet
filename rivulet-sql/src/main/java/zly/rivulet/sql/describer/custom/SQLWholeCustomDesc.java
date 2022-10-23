package zly.rivulet.sql.describer.custom;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.custom.CustomCollector;
import zly.rivulet.base.describer.custom.CustomDesc;
import zly.rivulet.base.describer.custom.CustomSingleValueWrap;
import zly.rivulet.base.describer.param.Param;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Description 整个语句的自定义语句，仅能提供param的解析
 *
 * @author zhaolaiyuan
 * Date 2022/10/23 13:41
 **/
public class SQLWholeCustomDesc implements CustomDesc {

    private final List<Param<?>> paramList;

    private final BiConsumer<CustomCollector, List<CustomSingleValueWrap>> biConsumer;

    public SQLWholeCustomDesc(List<Param<?>> paramList, BiConsumer<CustomCollector, List<CustomSingleValueWrap>> biConsumer) {
        this.paramList = paramList;
        this.biConsumer = biConsumer;
    }

    @Override
    public List<SingleValueElementDesc<?, ?>> getSingleValueList() {
        return (List)this.paramList;
    }

    @Override
    public BiConsumer<CustomCollector, List<CustomSingleValueWrap>> getCustomCollect() {
        return biConsumer;
    }
}
