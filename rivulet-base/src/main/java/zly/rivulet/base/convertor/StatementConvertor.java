package zly.rivulet.base.convertor;

public abstract class StatementConvertor<O> extends Convertor<O, String> {

    public StatementConvertor() {}

    public StatementConvertor(Class<O> originType) {
        super(originType, String.class);
    }

}
