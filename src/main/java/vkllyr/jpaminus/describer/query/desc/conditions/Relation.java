package vkllyr.jpaminus.describer.query.desc.conditions;

public class Relation<B, P> extends Condition<B, P> {

    private Condition<B, P>[] conditions;

    private String connector;

    public Relation(Condition<B, P>[] conditions, String connector) {
        super(null, null, null);
        this.conditions = conditions;
        this.connector = connector;
    }

    public static <B, P> Relation<B, P> and(Condition<B, P> ... conditions) {
        return new Relation<>(conditions, "AND");
    }

    public static <B, P> Relation<B, P> or(Condition<B, P> ... conditions) {
        return new Relation<>(conditions, "OR");
    }
}
