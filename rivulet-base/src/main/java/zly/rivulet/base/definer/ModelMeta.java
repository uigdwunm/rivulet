package zly.rivulet.base.definer;

import zly.rivulet.base.utils.View;

public interface ModelMeta {

    Class<?> getModelClass();

    /**
     * Description 获取model代理对象，空对象，只能用于解析get方法名，
     *
     * @author zhaolaiyuan
     * Date 2022/3/13 12:00
     **/
    Object getProxy();

    View<FieldMeta> getFieldMetaList();

    FieldMeta getFieldMetaByFieldName(String fieldName);
}
