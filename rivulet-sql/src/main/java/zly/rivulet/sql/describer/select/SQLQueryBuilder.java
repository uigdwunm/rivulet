package zly.rivulet.sql.describer.select;


import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.definer.meta.SQLColumnMeta;
import zly.rivulet.sql.definer.meta.SQLQueryMeta;
import zly.rivulet.sql.definer.meta.SQLSubQueryMeta;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.describer.custom.SQLPartCustomDesc;
import zly.rivulet.sql.describer.query_.SQLQueryMetaDesc;
import zly.rivulet.sql.describer.select.item.JoinItem;
import zly.rivulet.sql.describer.select.item.Mapping;
import zly.rivulet.sql.describer.select.item.SortItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLQueryBuilder<T> {

    protected Class<T> resultModelClass;

    protected boolean isOneResult = false;

    protected List<Mapping<T>> mappedItemList = null;

    protected SQLQueryMeta from;

    protected List<JoinItem> joinList = new ArrayList<>();

    protected ConditionContainer whereConditionContainer;

    protected List<SQLColumnMeta<?>> groupColumnList = null;

    protected ConditionContainer havingConditionContainer;

    /**
     * order排序子项
     **/
    protected List<SortItem> orderItemList;

    /**
     * 跳过行数
     **/
    protected Param<Integer> skit;

    /**
     * 查询行数
     **/
    protected Param<Integer> limit;

    /**
     * 如果有自定义语句，后面解析的时候会替代原Definition进行解析
     **/
    protected Map<Class<? extends Definition>, Param<SQLPartCustomDesc>> customStatementMap = new HashMap<>();

    public static <T extends SQLSubQueryMeta> SubQuerySelectByBuilder<T> subQuery(Class<T> clazz) {
        SubQuerySelectByBuilder<T> selectByBuilder = new SubQuerySelectByBuilder<>();
        selectByBuilder.resultModelClass = clazz;
        return selectByBuilder;
    }

    public static <T> SelectByBuilder<T> mappingTo(Class<T> clazz) {
        SelectByBuilder<T> selectBuilder = new SelectByBuilder<>();
        selectBuilder.resultModelClass = clazz;
        return selectBuilder;
    }

    public SQLQueryMetaDesc<T> build() {
        return new SQLQueryMetaDesc<>(
                this.resultModelClass,
                this.isOneResult,
                this.mappedItemList,
                this.from,
                this.joinList,
                this.whereConditionContainer,
                this.groupColumnList,
                this.havingConditionContainer,
                this.orderItemList,
                this.skit,
                this.limit,
                this.customStatementMap
        );
    }
}