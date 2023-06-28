package zly.rivulet.sql.describer.select;


import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.describer.custom.SQLPartCustomDesc;
import zly.rivulet.sql.describer.meta.SQLColumnMeta;
import zly.rivulet.sql.describer.meta.SQLTableMeta;
import zly.rivulet.sql.describer.query.SQLQueryMetaDesc;
import zly.rivulet.sql.describer.select.item.JoinItem;
import zly.rivulet.sql.describer.select.item.Mapping;
import zly.rivulet.sql.describer.select.item.SortItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLQueryBuilder<T> {

    protected Class<T> resultModelClass;

    /**
     * 结果自动填充模型（默认不自动填充）
     * 如果发现重复会报错
     **/
    protected boolean autoFillModel = false;

    /**
     * 结果自动填充模型时，自动转换下划线和驼峰式命名方式（默认不忽略）
     * 如果发现重复会报错
     **/
    protected boolean fillModelAutoConvertUnderline = false;

    /**
     * 结果自动填充模型时，忽略大小写（默认不忽略）
     * 如果发现重复会报错
     **/
    protected boolean fillModelIgnoreCase = false;

    protected List<Mapping<?>> mappedItemList;

    protected SQLTableMeta from;

    protected List<JoinItem> joinList = new ArrayList<>();

    protected ConditionContainer whereConditionContainer;

    protected List<SQLColumnMeta> groupColumnList = null;

    protected ConditionContainer hivingConditionContainer;

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


    public static <T> SelectByBuilder<T> mappingTo(Class<T> clazz) {
        SelectByBuilder<T> selectBuilder = new SelectByBuilder<>();
        selectBuilder.resultModelClass = clazz;
        return selectBuilder;
    }



    public SQLQueryMetaDesc<T> build() {

    }
}