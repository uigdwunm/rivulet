package zly.rivulet.sql.preparser;

import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.definer.annotations.SqlQueryAlias;

import java.lang.reflect.Field;
import java.util.*;

public class SQLAliasManager {

    private final Map<AliasFlag, String> aliasMap = new HashMap<>();

    private final Map<AliasFlag, String> shortAliasMap = new HashMap<>();

    private final Set<AliasFlag> allAliasSet;

    /**
     * 查询中存在子查询的情况，对这类可能存在嵌套的，则把表别名的嵌套关系保留起来
     * key 表示子查询的AliasFlag，value表示这个子查询AliasFlag对应的父查询的AliasFlag
     * 这里只有表别名，没有字段别名
     * 最外层的表别名是没有对应的父查询的
     **/
    private final Map<AliasFlag, AliasFlag> aliasToParentAlias;

    private final SqlRivuletProperties configProperties;

    public SQLAliasManager(SqlRivuletProperties configProperties, Set<AliasFlag> allAliasSet, Map<AliasFlag, AliasFlag> aliasToParentAlias) {
        this.configProperties = configProperties;
        this.allAliasSet = allAliasSet;
        this.aliasToParentAlias = aliasToParentAlias;
        initAlias();
    }

    private void initAlias() {
        Map<String, Integer> repeatAlias = new HashMap<>();
        for (AliasFlag aliasFlag : allAliasSet) {
            String alias = aliasFlag.getAlias();
            String fieldName = aliasFlag.getFieldName();
            Integer count = repeatAlias.compute(fieldName, (k, v) -> {
                if (v == null) {
                    return 0;
                }
                return v + 1;
            });
            if (alias != null) {
                this.aliasMap.put(aliasFlag, alias);
            } else {
                this.aliasMap.put(aliasFlag, fieldName + '_' + count);
            }
            int tableCount = repeatAlias.size();
            String shortName;
            if (tableCount < 26) {
                shortName = String.valueOf((char) (tableCount + 'a'));
            } else {
                // 最高支持一个查询里有 26*26 个表
                int aa = tableCount / 26;
                int bb = tableCount % 26;
                shortName = "" + (char) aa + (char) bb;
            }
            this.shortAliasMap.put(aliasFlag, shortName + '_' + count);
        }
    }

    public static AliasFlag createFlag(Field field) {
        SqlQueryAlias alias = field.getAnnotation(SqlQueryAlias.class);
        return new AliasFlag(alias != null ? alias.value() : null, field.getName());
    }

    /**
     * Description 用flag来换取别名
     *
     * @author zhaolaiyuan
     * Date 2022/5/14 15:45
     **/
    public String getAlias(AliasFlag aliasFlag) {
        // 找到最顶层的别名，因为每个子查询都有自己AliasManager，所有最顶层的一定是对应正确的
        AliasFlag parent = aliasToParentAlias.get(aliasFlag);
        while (parent != null) {
            aliasFlag = parent;
            parent = aliasToParentAlias.get(aliasFlag);
        }
        if (configProperties.isUseShortAlias()) {
            return shortAliasMap.get(aliasFlag);
        } else {
            return aliasMap.get(aliasFlag);
        }
    }

    /**
     * Description 一个独立查询对象的唯一标识
     *
     * @author zhaolaiyuan
     * Date 2022/5/14 11:30
     **/
    public static class AliasFlag {

        /**
         * {@code @Alias} 注解中的名称，如果存在，则别名一定是这个
         **/
        private final String alias;

        /**
         * 原字段名
         **/
        private final String fieldName;

        private AliasFlag(String alias, String fieldName) {
            this.alias = alias;
            this.fieldName = fieldName;
        }

        public String getAlias() {
            return alias;
        }

        public String getFieldName() {
            return fieldName;
        }
    }
}
