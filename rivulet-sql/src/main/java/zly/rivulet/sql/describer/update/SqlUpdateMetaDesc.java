package zly.rivulet.sql.describer.update;

import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.describer.query.desc.Mapping;

import java.util.List;

public class SqlUpdateMetaDesc<T> implements WholeDesc {

    /**
     * 要更新的模型
     **/
    private final Class<T> model;

    /**
     * set赋值列表
     **/
    private final List<Mapping<T, T, ?>> mappedItemList;


    /**
     * where查询子项
     **/
    private final ConditionContainer<T, ?> whereConditionContainer;

    protected RivuletDesc anno;

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
    public RivuletDesc getAnnotation() {
        return anno;
    }

    @Override
    public void setAnnotation(RivuletDesc anno) {
        this.anno = anno;
    }

    @Override
    public RivuletFlag getFlag() {
        return RivuletFlag.UPDATE;
    }

    @Override
    public Class<?> getReturnType() {
        return Integer.class;
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
