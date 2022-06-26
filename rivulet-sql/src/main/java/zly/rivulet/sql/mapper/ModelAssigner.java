package zly.rivulet.sql.mapper;

import zly.rivulet.base.describer.field.SelectMapping;

import java.util.List;

public class ModelAssigner extends Assigner {

    /**
     * 把自己塞到父对象的assigner
     **/
    private final List<SelectMapping<Object, Object>> fieldAssignerList;

    public ModelAssigner(SelectMapping<Object, Object> assigner, List<SelectMapping<Object, Object>> fieldAssignerList, int indexStart) {
        super(assigner, containerCreator, indexStart);
        this.indexStart = indexStart;
        this.assigner = assigner;
        this.fieldAssignerList = fieldAssignerList;
    }

    @Override
    public Object assign(List<Object> resultValues) {
        return null;
    }
}
