package zly.rivulet.sql.describer.select;

import zly.rivulet.sql.describer.select.item.Mapping;

import java.util.Arrays;

public class SelectByBuilder<T> extends FromByBuilder<T> {

    /**
     * Description 填充模型并自动转换驼峰和下划线命名方式
     *
     * @author zhaolaiyuan
     * Date 2023/6/26 15:19
     **/
    @SafeVarargs
    public final FromByBuilder<T> selectAutoFillModel(Mapping<T>... mappings) {
        super.autoFillModel = true;
        super.fillModelAutoConvertUnderline = true;
        super.mappedItemList = Arrays.asList(mappings);
        return this;
    }

    public FromByBuilder<T> select(boolean autoFillModel, boolean fillModelAutoConvertUnderline, boolean fillModelIgnoreCase) {
        super.autoFillModel = autoFillModel;
        super.fillModelAutoConvertUnderline = fillModelAutoConvertUnderline;
        super.fillModelIgnoreCase = fillModelIgnoreCase;
        return this;
    }
}