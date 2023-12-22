package zly.rivulet.sql.definer.meta;

import zly.rivulet.base.definer.meta.ColumnMeta;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.condition.ConditionElement;
import zly.rivulet.sql.describer.condition.ConditionOperate;
import zly.rivulet.sql.describer.condition.common.Condition;
import zly.rivulet.sql.describer.function.SQLFunction;
import zly.rivulet.sql.describer.query_.SQLQueryMetaDesc;

public abstract class SQLColumnMeta<C> extends ColumnMeta<C> {

    protected final SQLQueryMeta sqlQueryMeta;

    protected SQLColumnMeta(String name, SQLQueryMeta sqlQueryMeta, Class<C> type) {
        super(name, type);
        this.sqlQueryMeta = sqlQueryMeta;
    }

    public SQLQueryMeta getSqlQueryMeta() {
        return sqlQueryMeta;
    }

    /**
     * =
     **/
    public Condition eq(SQLColumnMeta<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.EQ, rightElement);
    }

    /**
     * =
     **/
    public Condition eq(Param<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.EQ, rightElement);
    }

    /**
     * =
     **/
    public Condition eq(SQLFunction<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.EQ, rightElement);
    }

    /**
     * =
     **/
    public Condition eq(SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.EQ, rightElement);
    }

    /**
     * !=
     **/
    public Condition ne(SQLColumnMeta<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.NE, rightElement);
    }

    /**
     * !=
     **/
    public Condition ne(Param<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.NE, rightElement);
    }

    /**
     * !=
     **/
    public Condition ne(SQLFunction<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.NE, rightElement);
    }

    /**
     * !=
     **/
    public Condition ne(SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.NE, rightElement);
    }

    /**
     * like
     **/
    public Condition like(SQLColumnMeta<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.LIKE, rightElement);
    }

    /**
     * like
     **/
    public Condition like(Param<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.LIKE, rightElement);
    }

    /**
     * like
     **/
    public Condition like(SQLFunction<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.LIKE, rightElement);
    }

    /**
     * like
     **/
    public Condition like(SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.LIKE, rightElement);
    }

    /**
     * not like
     **/
    public Condition notLike(SQLColumnMeta<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.NOT_LIKE, rightElement);
    }

    /**
     * not like
     **/
    public Condition notLike(Param<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.NOT_LIKE, rightElement);
    }

    /**
     * not like
     **/
    public Condition notLike(SQLFunction<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.NOT_LIKE, rightElement);
    }

    /**
     * not like
     **/
    public Condition notLike(SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.NOT_LIKE, rightElement);
    }

    /**
     * >
     **/
    public Condition gt(SQLColumnMeta<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.GT, rightElement);
    }

    /**
     * >
     **/
    public Condition gt(Param<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.GT, rightElement);
    }

    /**
     * >
     **/
    public Condition gt(SQLFunction<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.GT, rightElement);
    }

    /**
     * >
     **/
    public Condition gt(SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.GT, rightElement);
    }

    /**
     * >=
     **/
    public Condition gte(SQLColumnMeta<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.GTE, rightElement);
    }

    /**
     * >=
     **/
    public Condition gte(Param<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.GTE, rightElement);
    }

    /**
     * >=
     **/
    public Condition gte(SQLFunction<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.GTE, rightElement);
    }

    /**
     * >=
     **/
    public Condition gte(SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.GTE, rightElement);
    }

    /**
     * <
     **/
    public Condition lt(SQLColumnMeta<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.LT, rightElement);
    }

    /**
     * <
     **/
    public Condition lt(Param<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.LT, rightElement);
    }

    /**
     * <
     **/
    public Condition lt(SQLFunction<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.LT, rightElement);
    }

    /**
     * <
     **/
    public Condition lt(SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.LT, rightElement);
    }

    /**
     * <=
     **/
    public Condition lte(SQLColumnMeta<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.LTE, rightElement);
    }

    /**
     * <=
     **/
    public Condition lte(Param<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.LTE, rightElement);
    }

    /**
     * <=
     **/
    public Condition lte(SQLFunction<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.LTE, rightElement);
    }

    /**
     * <=
     **/
    public Condition lte(SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.LTE, rightElement);
    }

    /**
     * between and
     **/
    public Condition between(SQLColumnMeta<?> beforeElement, SQLColumnMeta<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * between and
     **/
    public Condition between(SQLColumnMeta<?> beforeElement, Param<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * between and
     **/
    public Condition between(SQLColumnMeta<?> beforeElement, SQLFunction<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * between and
     **/
    public Condition between(SQLColumnMeta<?> beforeElement, SQLQueryMetaDesc<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * between and
     **/
    public Condition between(Param<?> beforeElement, SQLColumnMeta<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * between and
     **/
    public Condition between(Param<?> beforeElement, Param<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * between and
     **/
    public Condition between(Param<?> beforeElement, SQLFunction<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * between and
     **/
    public Condition between(Param<?> beforeElement, SQLQueryMetaDesc<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * between and
     **/
    public Condition between(SQLFunction<?> beforeElement, SQLColumnMeta<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * between and
     **/
    public Condition between(SQLFunction<?> beforeElement, Param<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * between and
     **/
    public Condition between(SQLFunction<?> beforeElement, SQLFunction<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * between and
     **/
    public Condition between(SQLFunction<?> beforeElement, SQLQueryMetaDesc<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * between and
     **/
    public Condition between(SQLQueryMetaDesc<?> beforeElement, SQLColumnMeta<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * between and
     **/
    public Condition between(SQLQueryMetaDesc<?> beforeElement, Param<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * between and
     **/
    public Condition between(SQLQueryMetaDesc<?> beforeElement, SQLFunction<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * between and
     **/
    public Condition between(SQLQueryMetaDesc<?> beforeElement, SQLQueryMetaDesc<?> afterElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.BETWEEN, beforeElement, afterElement);
    }

    /**
     * in
     **/
    public Condition in(Param<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.IN, rightElement);
    }

    /**
     * in
     **/
    public Condition in(SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.IN, rightElement);
    }

    /**
     * not in
     **/
    public Condition notIn(Param<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.NOT_IN, rightElement);
    }

    /**
     * not in
     **/
    public Condition notIn(SQLQueryMetaDesc<?> rightElement) {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.NOT_IN, rightElement);
    }

    /**
     * is null
     **/
    public Condition isNull() {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.IS_NULL);
    }

    /**
     * not null
     **/
    public Condition notNull() {
        return new ConditionElement(CheckCondition.IS_TRUE, this, ConditionOperate.NOT_NULL);
    }
}
