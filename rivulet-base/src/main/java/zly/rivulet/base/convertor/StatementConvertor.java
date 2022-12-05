package zly.rivulet.base.convertor;

import zly.rivulet.base.definer.outerType.OriginOuterType;

public abstract class StatementConvertor<O> extends Convertor<O, String> {

    private Class<? extends OriginOuterType> originOuterType;

    public StatementConvertor(Class<? extends OriginOuterType> originOuterType) {
        this.originOuterType = originOuterType;
    }

    public Class<? extends OriginOuterType> getOriginOuterType() {
        return originOuterType;
    }
}
