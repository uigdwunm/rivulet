package zly.rivulet.sql.describer.select;

import zly.rivulet.sql.definition.query.join.JoinType;
import zly.rivulet.sql.definition.query.join.SQLJoinType;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.definer.meta.SQLQueryMeta;
import zly.rivulet.sql.describer.select.item.JoinItem;

import java.util.Arrays;

public class JoinByBuilder<T> extends WhereByBuilder<T> {

    public final JoinByBuilder<T> leftJoin(SQLQueryMeta join, Condition ... onCondition) {
        return this.leftJoin(join, new ConditionContainer.AND(Arrays.asList(onCondition)));
    }

    public final JoinByBuilder<T> rightJoin(SQLQueryMeta join, Condition ... onCondition) {
        return this.rightJoin(join, new ConditionContainer.AND(Arrays.asList(onCondition)));
    }

    public final JoinByBuilder<T> innerJoin(SQLQueryMeta join, Condition ... onCondition) {
        return this.innerJoin(join, new ConditionContainer.AND(Arrays.asList(onCondition)));
    }

    public final JoinByBuilder<T> leftJoin(SQLQueryMeta join, ConditionContainer onCondition) {
        return this.join(SQLJoinType.LEFT_JOIN, join, onCondition);
    }

    public final JoinByBuilder<T> rightJoin(SQLQueryMeta join, ConditionContainer onCondition) {
        return this.join(SQLJoinType.RIGHT_JOIN, join, onCondition);
    }

    public final JoinByBuilder<T> innerJoin(SQLQueryMeta join, ConditionContainer onCondition) {
        return this.join(SQLJoinType.INNER_JOIN, join, onCondition);
    }

    private JoinByBuilder<T> join(JoinType joinType, SQLQueryMeta join, ConditionContainer onCondition) {
        super.joinList.add(new JoinItem(joinType, join, onCondition));
        return this;
    }
}
