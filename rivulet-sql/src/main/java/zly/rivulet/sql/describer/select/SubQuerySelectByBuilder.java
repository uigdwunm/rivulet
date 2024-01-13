package zly.rivulet.sql.describer.select;

import zly.rivulet.sql.describer.select.item.SubQueryMapping;

import java.util.Arrays;

public class SubQuerySelectByBuilder<T> extends FromByBuilder<T> {

    @SafeVarargs
    public final FromByBuilder<T> select(SubQueryMapping<T>... mappings) {
        super.mappedItemList = Arrays.asList(mappings);
        return this;
    }

    /**
     * Description 填充模型并自动匹配，转换驼峰和下划线命名忽略大小写
     *
     * @author zhaolaiyuan
     * Date 2023/6/26 15:19
     **/
    public final FromByBuilder<T> selectAutoFillModel() {
        return this;
    }

}