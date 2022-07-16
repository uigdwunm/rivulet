package zly.rivulet.sql.definition.query.join;

public enum SQLJoinType implements JoinType {
    LEFT_JOIN("LEFT JOIN"),
    RIGHT_JOIN("RIGHT JOIN"),
    INNER_JOIN("LEFT JOIN")
    ;

    private final String prefix;

    SQLJoinType(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }
}
