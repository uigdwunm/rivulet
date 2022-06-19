package zly.rivulet.sql.describer.join;

import zly.rivulet.sql.describer.query.desc.JoinCondition;

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

    private final List<Relation<?>> leftJoinRelations = new ArrayList<>();

    private final List<Relation<?>> rightJoinRelations = new ArrayList<>();

    private final List<Relation<?>> innerJoinRelations = new ArrayList<>();

    private ComplexDescriber(Object modelFrom) {
        this.modelFrom = modelFrom;
    }

    public static ComplexDescriber from(Object modelFrom) {
        return new ComplexDescriber(modelFrom);
    }

    public <R> Relation<R> leftJoin(R joinModel) {
        Relation<R> leftJoin = new Relation<>(joinModel);
        this.leftJoinRelations.add(leftJoin);
        return leftJoin;
    }

    public Object getModelFrom() {
        return modelFrom;
    }

    public List<Relation<?>> getLeftJoinRelations() {
        return leftJoinRelations;
    }

    public List<Relation<?>> getRightJoinRelations() {
        return rightJoinRelations;
    }

    public List<Relation<?>> getInnerJoinRelations() {
        return innerJoinRelations;
    }

    public static class Relation<R> {

        /**
         * Description 关联对象
         *
         * @author zhaolaiyuan
         * Date 2022/5/15 11:10
         **/
        private final R modelRelation;

        private final List<JoinCondition<?, ?, ?>> conditionList = new ArrayList<>();

        public Relation(R modelRelation) {
            this.modelRelation = modelRelation;
        }

        public final void on(JoinCondition<?, ?, ?>... conditions) {
            conditionList.addAll(Arrays.asList(conditions));
        }

        public R getModelRelation() {
            return modelRelation;
        }

        public List<JoinCondition<?, ?, ?>> getConditionList() {
            return conditionList;
        }
    }

}
