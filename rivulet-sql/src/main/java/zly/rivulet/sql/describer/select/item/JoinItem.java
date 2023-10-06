package zly.rivulet.sql.describer.select.item;

import zly.rivulet.sql.definition.query.join.JoinType;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.describer.meta.SQLQueryMeta;

public class JoinItem {

    private final JoinType joinType;

    private final SQLQueryMeta joinTable;

    protected ConditionContainer onConditionContainer;

    public JoinItem(JoinType joinType, SQLQueryMeta joinTable, ConditionContainer onConditionContainer) {
        this.joinType = joinType;
        this.joinTable = joinTable;
        this.onConditionContainer = onConditionContainer;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public SQLQueryMeta getJoinTable() {
        return joinTable;
    }

    public ConditionContainer getOnConditionContainer() {
        return onConditionContainer;
    }
}
