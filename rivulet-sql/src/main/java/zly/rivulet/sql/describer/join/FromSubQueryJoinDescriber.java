package zly.rivulet.sql.describer.join;

import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;

public class FromSubQueryJoinDescriber<F> extends JoinDescriber<F> {

    private final SqlQueryMetaDesc<?, F> subQueryFrom;

    public FromSubQueryJoinDescriber(SqlQueryMetaDesc<?, F> subQueryFrom) {
        this.subQueryFrom = subQueryFrom;
    }
}
