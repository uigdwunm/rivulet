package zly.rivulet.sql.describer.delete;

import zly.rivulet.base.definer.annotations.RivuletDesc;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;

public class SQLDeleteMetaDesc<T> implements WholeDesc {

    /**
     * 要更新的模型
     **/
    private final Class<T> model;

    /**
     * where查询子项
     **/
    private final ConditionContainer<T, ?> whereConditionContainer;

    protected RivuletDesc anno;

    public SQLDeleteMetaDesc(Class<T> model, ConditionContainer<T, ?> whereConditionContainer) {
        this.model = model;
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

    public ConditionContainer<T, ?> getWhereConditionContainer() {
        return whereConditionContainer;
    }
}
