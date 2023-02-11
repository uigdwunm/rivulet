package zly.rivulet.sql.describer.query.builder;

import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.definition.query.main.OrderByDefinition;
import zly.rivulet.sql.describer.custom.SQLPartCustomDesc;
import zly.rivulet.sql.describer.query.desc.SortItem;

import java.util.Arrays;
import java.util.List;

public class OrderByBuilderSQL<F, S> extends SkitBuilderSQL<F, S> {

    @SafeVarargs
    public final SkitBuilderSQL<F, S> orderBy(SortItem<F, ?> ... items) {
        super.orderItemList = Arrays.asList(items);
        return this;
    }

    /**
     * Description 在入参中指定排序规则
     *
     * @author zhaolaiyuan
     * Date 2022/10/23 11:44
     **/
    public final SkitBuilderSQL<F, S> orderBy(Param<SQLPartCustomDesc> customDescParam) {
        super.customStatementMap.put(OrderByDefinition.class, customDescParam);
        return this;
    }

    /**
     * Description 在入参中指定排序规则，在加个默认值
     *
     * @author zhaolaiyuan
     * Date 2022/10/23 11:44
     **/
    public final SkitBuilderSQL<F, S> orderBy(Param<SQLPartCustomDesc> customDescParam, List<SortItem<F, ?>> orderItemList) {
        super.customStatementMap.put(OrderByDefinition.class, customDescParam);
        super.orderItemList = orderItemList;
        return this;
    }
}
