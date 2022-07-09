package zly.rivulet.sql.preparser;

import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.definer.annotations.SqlQueryAlias;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.preparser.helper.node.FromNode;
import zly.rivulet.sql.preparser.helper.node.ProxyNode;
import zly.rivulet.sql.preparser.helper.node.QueryProxyNode;
import zly.rivulet.sql.preparser.helper.node.SelectNode;

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

    private SQLAliasManager(SqlRivuletProperties configProperties, Set<AliasFlag> allAliasSet, Map<AliasFlag, AliasFlag> aliasToParentAlias) {
        this.configProperties = configProperties;
        this.allAliasSet = allAliasSet;
        this.aliasToParentAlias = aliasToParentAlias;
        initAlias();
    }

    public static SQLAliasManager create(SqlRivuletProperties configProperties, QueryProxyNode queryProxyNode) {
        Set<AliasFlag> allAliasSet = new HashSet<>();
        Map<AliasFlag, AliasFlag> aliasToParentAlias = new HashMap<>();
        create(allAliasSet, aliasToParentAlias, queryProxyNode);
        return new SQLAliasManager(configProperties, allAliasSet, aliasToParentAlias);
    }

    private static void create(Set<AliasFlag> allAliasSet, Map<AliasFlag, AliasFlag> aliasToParentAlias, ProxyNode proxyNode) {
        if (proxyNode instanceof QueryProxyNode) {
            QueryProxyNode queryProxyNode = (QueryProxyNode) proxyNode;
            for (FromNode fromNode : queryProxyNode.getFromNodeList()) {
                allAliasSet.add(fromNode.getAliasFlag());
                ProxyNode parentNode = fromNode.getParentNode();
                if (parentNode != null) {
                    aliasToParentAlias.put(fromNode.getAliasFlag(), parentNode.getAliasFlag());
                }
                create(allAliasSet, aliasToParentAlias, fromNode);
            }
            for (SelectNode selectNode : queryProxyNode.getSelectNodeList()) {

            }

            for (QueryProxyNode whereNode : queryProxyNode.getWhereSubQueryList()) {

            }

        }

    }

    private void initAlias() {
        Map<String, Integer> repeatAlias = new HashMap<>();
        for (AliasFlag aliasFlag : allAliasSet) {
            String alias = aliasFlag.alias;
            boolean isForce = aliasFlag.isForce;
            Integer count = repeatAlias.compute(alias, (k, v) -> {
                if (v == null) {
                    return 0;
                }
                return v + 1;
            });
            if (isForce) {
                this.aliasMap.put(aliasFlag, alias);
            } else {
                String put = this.aliasMap.put(aliasFlag, alias + '_' + count);
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

    public static AliasFlag createModelAlias(String alias, SQLModelMeta sqlModelMeta) {
        return new AliasFlag(alias, false, sqlModelMeta, false);
    }

    public static AliasFlag createModelAlias(SqlQueryAlias sqlQueryAlias, SQLModelMeta sqlModelMeta) {
        return new AliasFlag(sqlQueryAlias.value(), true, sqlModelMeta, false);
    }

    public static AliasFlag createModelAlias(SqlQueryAlias sqlQueryAlias) {
        return new AliasFlag(sqlQueryAlias.value(), true, null, false);
    }

    public static AliasFlag createModelAlias(String suggestedAlias) {
        return new AliasFlag(suggestedAlias, false, null, false);
    }

    public static AliasFlag createModelAlias() {
        return new AliasFlag(null, false, null, false);
    }

    public static AliasFlag createFieldAlias(SqlQueryAlias sqlQueryAlias) {
        return new AliasFlag(sqlQueryAlias.value(), true, null, true);
    }

    public static AliasFlag createFieldAlias(String suggestedAlias) {
        return new AliasFlag(suggestedAlias, false, null, true);
    }

    public static AliasFlag createFieldAlias() {
        return new AliasFlag(null, false, null, true);
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
         * 别名建议值，可能是{@link SqlQueryAlias}注解指定的，可能是query中的字段名
         * 可能为空，为空表示随便指定一个
         **/
        private String alias;

        /**
         * 是否强制使用上面的别名，如果是通过 {@link SqlQueryAlias}注解 指定的就是强制使用的
         **/
        private boolean isForce;

        /**
         * 如果对应到一个表，则记录
         **/
        private final ModelMeta modelMeta;

        /**
         * 是否是字段
         **/
        private final boolean isField;

        private AliasFlag(String alias, boolean isForce, ModelMeta modelMeta, boolean isField) {
            this.alias = alias;
            this.isForce = isForce;
            this.modelMeta = modelMeta;
            this.isField = isField;
        }

        private AliasFlag(ModelMeta modelMeta, boolean isField) {
            this.alias = null;
            this.isForce = false;
            this.modelMeta = modelMeta;
            this.isField = isField;
        }

        public void suggestedAlias(String alias) {
            if (StringUtil.isBlank(alias)) {
                return;
            }
            this.alias = alias;
            this.isForce = false;
        }

        public void forceAlias(String alias) {
            if (isForce) {
                throw UnbelievableException.unbelievable();
            }
            this.alias = alias;
            this.isForce = true;
        }

        public String getAlias() {
            return alias;
        }

        public boolean isForce() {
            return isForce;
        }

        public ModelMeta getModelMeta() {
            return modelMeta;
        }

        public boolean isField() {
            return isField;
        }
    }
}
