package zly.rivulet.sql.parser;

import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.definer.annotations.SqlQueryAlias;
import zly.rivulet.sql.exception.SQLDescDefineException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SQLAliasManager {

    private final Map<AliasFlag, String> aliasMap = new HashMap<>();

    private final Map<AliasFlag, String> shortAliasMap = new HashMap<>();

    private final Set<AliasFlag> allAliasSet = new HashSet<>();

    /**
     * 查询中存在子查询的情况，对这类可能存在嵌套的，则把表别名的嵌套关系保留起来
     * key 表示子查询的AliasFlag，value表示这个子查询AliasFlag对应的父查询的AliasFlag
     * 这里只有表别名，没有字段别名
     * 最外层的表别名是没有对应的父查询的
     **/
    private final Map<AliasFlag, AliasFlag> aliasToParentAlias = new HashMap<>();

    private final SqlRivuletProperties configProperties;

    public SQLAliasManager(SqlRivuletProperties configProperties) {
        this.configProperties = configProperties;
    }

    public void initAlias() {
        Map<String, Integer> repeatAlias = new HashMap<>();
        Set<String> forceAliasCheck = new HashSet<>();
        for (AliasFlag aliasFlag : allAliasSet) {
            String forceAlias = aliasFlag.getForceAlias();
            if (StringUtil.isNotBlank(forceAlias)) {
                // 强制指定的别名，这里检查下
                if (!forceAliasCheck.add(forceAlias)) {
                    throw SQLDescDefineException.forceAliasRepeat(forceAlias);
                }
                aliasMap.put(aliasFlag, forceAlias);
                shortAliasMap.put(aliasFlag, forceAlias);
            } else {
                // 非强制指定的别名，这里保存并生成短别名
                String alias = aliasFlag.getSuggestAlias();
                int repeatCount = repeatAlias.compute(alias, (k, v) -> {
                    if (v == null) {
                        return 0;
                    }
                    return v + 1;
                });
                if (repeatCount == 0) {
                    this.aliasMap.put(aliasFlag, alias);
                } else {
                    this.aliasMap.put(aliasFlag, alias + '_' + repeatCount);
                }

                shortAliasMap.put(aliasFlag, this.generateShortAlias(repeatAlias, repeatCount));
            }

        }
    }


    /**
     * Description 生成短别名的规则
     *
     * @author zhaolaiyuan
     * Date 2022/10/6 11:30
     **/
    private String generateShortAlias(Map<String, Integer> repeatAlias, int repeatCount) {
        int tableCount = repeatAlias.size();
        String shortName;
        if (tableCount < 26) {
            shortName = String.valueOf((char) (tableCount - 1 + 'a'));
        } else {
            // 最高支持一个查询里有 26*26 个表
            int aa = tableCount / 26;
            int bb = tableCount % 26;
            shortName = "" + (char) aa + (char) bb;
        }
        if (repeatCount == 0) {
            return shortName;
        } else {
            return shortName + '_' + repeatCount;
        }

    }

    public AliasFlag createAlias(SqlQueryAlias sqlQueryAlias) {
        AliasFlag aliasFlag = new AliasFlag(sqlQueryAlias.value(), null);
        this.allAliasSet.add(aliasFlag);
        return aliasFlag;
    }

    public AliasFlag createAlias(String suggestedAlias) {
        AliasFlag aliasFlag = new AliasFlag(null, suggestedAlias);
        this.allAliasSet.add(aliasFlag);
        return aliasFlag;
    }

    public AliasFlag createAlias() {
        AliasFlag aliasFlag = new AliasFlag(null, null);
        this.allAliasSet.add(aliasFlag);
        return aliasFlag;
    }

    public void addRelation(AliasFlag child, AliasFlag parent) {
        aliasToParentAlias.put(child, parent);
    }

    public void addAllSubAlias(SQLAliasManager subAliasManager) {
        this.allAliasSet.addAll(subAliasManager.allAliasSet);
    }

    /**
     * Description 用flag来换取别名
     *
     * @author zhaolaiyuan
     * Date 2022/5/14 15:45
     **/
    public String getAlias(AliasFlag aliasFlag) {
        if (aliasFlag == null) {
            return null;
        }
        // 找到最顶层的别名，因为每个子查询都有自己AliasManager，所有最顶层的一定是对应正确的
//        AliasFlag parent = aliasToParentAlias.get(aliasFlag);
//        while (parent != null) {
//            aliasFlag = parent;
//            parent = aliasToParentAlias.get(aliasFlag);
//        }
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
         * 强制别名值
         * 可能为空，为空表示不强制
         **/
        private String forceAlias;

        /**
         * 建议别名值
         * 可能为空，为空表示随便给
         **/
        private String suggestAlias;

        private AliasFlag(String forceAlias, String suggestAlias) {
            this.forceAlias = forceAlias;
            this.suggestAlias = suggestAlias;
        }

        public void suggestedAlias(String alias) {
            if (StringUtil.isNotBlank(this.forceAlias) && StringUtil.isNotBlank(this.suggestAlias)) {
                return;
            }
            this.suggestAlias = alias;
        }

        public void forceAlias(String alias) {
            if (StringUtil.isNotBlank(this.forceAlias)) {
                throw UnbelievableException.unbelievable();
            }
            this.forceAlias = alias;
        }

        public String getForceAlias() {
            return forceAlias;
        }

        public String getSuggestAlias() {
            return suggestAlias;
        }
    }
}
