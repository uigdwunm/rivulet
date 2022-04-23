package zly.rivulet.sql.describer.join;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;
import zly.rivulet.sql.describer.query.desc.Condition;
import zly.rivulet.sql.describer.query.desc.ConditionOperate;

import java.util.ArrayList;
import java.util.List;

/**
 * Description 描述一个联表的关系
 *
 * @author zhaolaiyuan
 * Date 2022/4/4 16:23
 **/
public abstract class JoinDescriber<F> {

    private Relation<?> leftJoinRelation;

    private Relation<?> rightJoinRelation;

    private Relation<?> innerJoinRelation;

    public static <F> FromModelJoinDescriber<F> mainFrom(Class<F> modelFrom) {
        return new FromModelJoinDescriber<>(modelFrom);
    }

    public static <F> FromSubQueryJoinDescriber<F> subQueryFrom(SqlQueryMetaDesc<?, F> subQueryFrom) {
        return new FromSubQueryJoinDescriber<>(subQueryFrom);
    }


    public <R> Relation<R> leftJoin(Class<R> modelFrom) {
        return new ModelRelation<>(modelFrom);
    }

    public <R> Relation<R> leftJoin(SqlQueryMetaDesc<?, R> subQueryFrom) {
        return new SubQueryRelation<>(subQueryFrom);
    }

    public abstract class Relation<R> {

        private final List<Condition<F, R, ?>> conditionList = new ArrayList<>();

        public <C> Condition<F, R, C> onEqualTo(SingleValueElementDesc<F, C> leftElement, SingleValueElementDesc<R, C> rightElement) {
            return of(leftElement, ConditionOperate.EQ, rightElement);
        }

        @SafeVarargs
        public final <C> Condition<F, R, C> of(SingleValueElementDesc<F, C> leftElement, ConditionOperate operate, SingleValueElementDesc<R, C> ... rightElements) {
            return new Condition<>(leftElement, operate, rightElements);
        }
    }

    private class ModelRelation<R> extends Relation<R> {
        /**
         * Description 关联表
         *
         * @author zhaolaiyuan
         * Date 2022/4/4 15:22
         **/
        private final Class<R> modelRelation;

        public ModelRelation(Class<R> modelRelation) {
            this.modelRelation = modelRelation;
        }
    }

    private class SubQueryRelation<R> extends Relation<R> {
        private final SqlQueryMetaDesc<?, R> subQueryRelation;

        private SubQueryRelation(SqlQueryMetaDesc<?, R> subQueryRelation) {
            this.subQueryRelation = subQueryRelation;
        }
    }
}
