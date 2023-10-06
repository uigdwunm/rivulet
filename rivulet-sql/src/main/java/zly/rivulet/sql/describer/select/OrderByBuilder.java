package zly.rivulet.sql.describer.select;

import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.definition.query_.main.OrderByDefinition;
import zly.rivulet.sql.describer.custom.SQLPartCustomDesc;
import zly.rivulet.sql.describer.select.item.SortItem;

import java.util.Arrays;
import java.util.List;

public class OrderByBuilder<T> extends SkitBuilder<T> {


    public final SkitBuilder<T> orderBy(SortItem ... items) {
        super.orderItemList = Arrays.asList(items);
        return this;
    }

    /**
     * Description 在入参中指定排序规则
     *
     * @author zhaolaiyuan
     * Date 2022/10/23 11:44
     **/
    public final SkitBuilder<T> orderBy(Param<SQLPartCustomDesc> customDescParam) {
        super.customStatementMap.put(OrderByDefinition.class, customDescParam);
        return this;
    }

    /**
     * Description 在入参中指定排序规则，在加个默认值
     *
     * @author zhaolaiyuan
     * Date 2022/10/23 11:44
     **/
    public final SkitBuilder<T> orderBy(Param<SQLPartCustomDesc> customDescParam, List<SortItem> orderItemList) {
        super.customStatementMap.put(OrderByDefinition.class, customDescParam);
        super.orderItemList = orderItemList;
        return this;
    }
}
