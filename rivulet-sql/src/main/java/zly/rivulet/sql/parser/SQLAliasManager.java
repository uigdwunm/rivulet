package zly.rivulet.sql.parser;

import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.definer.annotations.SqlQueryAlias;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.proxy_node.FromNode;
import zly.rivulet.sql.parser.proxy_node.ProxyNode;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.proxy_node.SelectNode;

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

    /**
     * Description 检测用，所有别名全部加载后丢弃
     *
     * @author zhaolaiyuan
     * Date 2022/10/7 19:58
     **/
    private Map<String, Integer> repeatAlias = new HashMap<>();

    /**
     * Description 检测用，所有别名全部加载完丢弃
     *
     * @author zhaolaiyuan
     * Date 2022/10/7 19:59
     **/
    private Set<String> forceAliasCheck = new HashSet<>();

    public SQLAliasManager(SqlRivuletProperties configProperties) {
        this.configProperties = configProperties;
    }

    public void init(QueryProxyNode queryProxyNode) {
        this.collect(queryProxyNode);
        this.initAlias();
    }

    private void collect(ProxyNode proxyNode) {
        if (proxyNode instanceof QueryProxyNode) {
            QueryProxyNode queryProxyNode = (QueryProxyNode) proxyNode;
            for (FromNode fromNode : queryProxyNode.getFromNodeList()) {
                allAliasSet.add(fromNode.getAliasFlag());
                ProxyNode parentNode = fromNode.getParentNode();
                if (parentNode != null) {
                    aliasToParentAlias.put(fromNode.getAliasFlag(), parentNode.getAliasFlag());
                }
                this.collect(fromNode);
            }
            for (SelectNode selectNode : queryProxyNode.getSelectNodeList()) {
                allAliasSet.add(selectNode.getAliasFlag());
                ProxyNode parentNode = selectNode.getParentNode();
                if (parentNode != null) {
                    aliasToParentAlias.put(selectNode.getAliasFlag(), parentNode.getAliasFlag());
                }
                this.collect(selectNode);
            }

            for (QueryProxyNode whereNode : queryProxyNode.getConditionSubQueryList()) {
                allAliasSet.add(whereNode.getAliasFlag());
                ProxyNode parentNode = whereNode.getParentNode();
                if (parentNode != null) {
                    aliasToParentAlias.put(whereNode.getAliasFlag(), parentNode.getAliasFlag());
                }
                this.collect(whereNode);
            }

        }
    }

    private void initAlias() {
        for (AliasFlag aliasFlag : allAliasSet) {
            // 保存并生成短别名
            this.suggestAlias(aliasFlag, aliasFlag.getSuggestAlias());
        }
    }

    public void forceAlias(AliasFlag aliasFlag, String forceAlias) {
        // 强制指定的别名，这里检查下
        if (!forceAliasCheck.add(forceAlias)) {
            throw SQLDescDefineException.forceAliasRepeat(forceAlias);
        }
        aliasMap.put(aliasFlag, forceAlias);
        shortAliasMap.put(aliasFlag, forceAlias);
    }

    public void suggestAlias(AliasFlag aliasFlag, String alias) {
        if (StringUtil.isBlank(alias)) {
            alias = "t";
        }
        // 非强制指定的别名，并且没有事先指定过，这里保存并生成短别名
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

        String shortAlias = this.generateShortAlias(repeatAlias, repeatCount);
        while (repeatAlias.containsKey(shortAlias)) {
            shortAlias = this.generateShortAlias(repeatAlias, ++repeatCount);
        }
        shortAliasMap.put(aliasFlag, shortAlias);
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

    public static AliasFlag createAlias(String suggestedAlias) {
        return new AliasFlag(suggestedAlias);
    }

    public static AliasFlag createAlias() {
        return new AliasFlag(null);
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
         * 建议别名值
         * 可能为空，为空表示随便给
         **/
        private String suggestAlias;

        private AliasFlag(String suggestAlias) {
            this.suggestAlias = suggestAlias;
        }

        public void suggestAlias(String alias) {
            if (StringUtil.isNotBlank(this.suggestAlias)) {
                return;
            }
            this.suggestAlias = alias;
        }

        public String getSuggestAlias() {
            return suggestAlias;
        }
    }
}
