package zly.rivulet.sql.describer.join;

import zly.rivulet.sql.definition.query.join.JoinType;
import zly.rivulet.sql.definition.query.join.SQLJoinType;
import zly.rivulet.sql.describer.query.condition.JoinCondition;
import zly.rivulet.sql.describer.query.condition.JoinConditionContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description 描述一个复杂查询（比如连表、子查询）的关系
 *
 * @author zhaolaiyuan
 * Date 2022/4/4 16:23
 **/
public class ComplexDescriber {
    /**
     * Description 主表的model，也可以是子查询
     *
     * @author zhaolaiyuan
     * Date 2022/4/4 15:22
     **/
    private final Object modelFrom;

    private final List<Relation<?>> joinRelations = new ArrayList<>();

    private ComplexDescriber(Object modelFrom) {
        this.modelFrom = modelFrom;
    }

    public static ComplexDescriber from(Object modelFrom) {
        return new ComplexDescriber(modelFrom);
    }

    public <R> Relation<R> leftJoin(R joinModel) {
        Relation<R> leftJoin = new Relation<>(joinModel, SQLJoinType.LEFT_JOIN);
        joinRelations.add(leftJoin);
        return leftJoin;
    }

    public List<Relation<?>> getJoinRelations() {
        return joinRelations;
    }

    public Object getModelFrom() {
        return modelFrom;
    }

    public static class Relation<R> {

        /**
         * Description 关联对象
         *
         * @author zhaolaiyuan
         * Date 2022/5/15 11:10
         **/
        private final R modelRelation;

        private final JoinType joinType;

        private JoinConditionContainer<?, ?> conditionContainer;

        public Relation(R modelRelation, JoinType joinType) {
            this.modelRelation = modelRelation;
            this.joinType = joinType;
        }

        public final void on(JoinCondition<?, ?>... conditions) {
            this.onAnd(conditions);
        }

        public final void onAnd(JoinCondition<?, ?>... conditions) {
            this.conditionContainer = new JoinConditionContainer.AND<>(Arrays.asList(conditions));
        }

        public final void onOr(JoinCondition<?, ?>... conditions) {
            this.conditionContainer = new JoinConditionContainer.OR<>(Arrays.asList(conditions));
        }

        public R getModelRelation() {
            return modelRelation;
        }

        public JoinConditionContainer<?, ?> getConditionContainer() {
            return conditionContainer;
        }

        public JoinType getJoinType() {
            return joinType;
        }
    }

}
