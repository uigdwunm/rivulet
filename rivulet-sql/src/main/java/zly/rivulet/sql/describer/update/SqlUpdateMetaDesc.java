package zly.rivulet.sql.describer.update;

import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.sql.describer.condition.ConditionContainer;
import zly.rivulet.sql.describer.query.desc.Mapping;

import java.util.List;

public class SqlUpdateMetaDesc<T> implements WholeDesc {

    /**
     * 要更新的模型
     **/
    private final Class<T> model;

    /**
     * 映射的查询列表，
     **/
    private final List<Mapping<T, T, ?>> mappedItemList;


    /**
     * where查询子项
     **/
    private final ConditionContainer<T, ?> whereConditionContainer;

    private String key;

    public SqlUpdateMetaDesc(Class<T> model, List<Mapping<T, T, ?>> mappedItemList, ConditionContainer<T, ?> whereConditionContainer) {
        this.model = model;
        this.mappedItemList = mappedItemList;
        this.whereConditionContainer = whereConditionContainer;
    }

    @Override
    public Class<?> getMainFrom() {
        return this.model;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    public Class<T> getModel() {
        return model;
    }

    public List<Mapping<T, T, ?>> getMappedItemList() {
        return mappedItemList;
    }

    public ConditionContainer<T, ?> getWhereConditionContainer() {
        return whereConditionContainer;
    }
}
