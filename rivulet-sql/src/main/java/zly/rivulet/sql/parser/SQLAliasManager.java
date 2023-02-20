package zly.rivulet.sql.parser;

import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.SQLRivuletProperties;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.proxy_node.FromNode;
import zly.rivulet.sql.parser.proxy_node.ProxyNode;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.proxy_node.SelectNode;

import java.util.HashMap;
import java.util.Map;

public class SQLAliasManager {

    private final SQLRivuletProperties configProperties;

    private final Map<AliasFlag, String> aliasMap;

    private final Map<AliasFlag, String> shortAliasMap;

    /**
     * 查询中存在子查询的情况，对这类可能存在嵌套的，则把表别名的嵌套关系保留起来
     * key 表示子查询的AliasFlag，value表示这个子查询AliasFlag对应的父查询的AliasFlag
     * 这里只有表别名，没有字段别名
     * 最外层的表别名是没有对应的父查询的
     **/
//    private final Map<AliasFlag, AliasFlag> aliasToParentAlias = new HashMap<>();

    /**
     * Description 检测用，所有别名全部加载后丢弃
     *
     * @author zhaolaiyuan
     * Date 2022/10/7 19:58
     **/
    private final Map<String, Integer> repeatAlias = new HashMap<>();

    public SQLAliasManager(SQLRivuletProperties configProperties) {
        this(configProperties, new HashMap<>(), new HashMap<>());
    }

    private SQLAliasManager(SQLRivuletProperties configProperties, Map<AliasFlag, String> aliasMap, Map<AliasFlag, String> shortAliasMap) {
        this.configProperties = configProperties;
        this.aliasMap = aliasMap;
        this.shortAliasMap = shortAliasMap;
    }

    public void init(QueryProxyNode queryProxyNode) {
        this.collect(queryProxyNode);
        this.initAlias();
    }

    private void collect(ProxyNode proxyNode) {
        if (proxyNode instanceof QueryProxyNode) {
            QueryProxyNode queryProxyNode = (QueryProxyNode) proxyNode;
            for (FromNode fromNode : queryProxyNode.getFromNodeList()) {
                aliasMap.putIfAbsent(fromNode.getAliasFlag(), null);
//                aliasToParentAlias.put(fromNode.getAliasFlag(), queryProxyNode.getAliasFlag());
                this.collect(fromNode);
            }
            for (SelectNode selectNode : queryProxyNode.getSelectNodeList()) {
                aliasMap.putIfAbsent(selectNode.getAliasFlag(), null);
//                aliasToParentAlias.put(selectNode.getAliasFlag(), queryProxyNode.getAliasFlag());
                this.collect(selectNode);
            }

            for (QueryProxyNode whereNode : queryProxyNode.getConditionSubQueryList()) {
                aliasMap.putIfAbsent(whereNode.getAliasFlag(), null);
//                aliasToParentAlias.put(whereNode.getAliasFlag(), queryProxyNode.getAliasFlag());
                this.collect(whereNode);
            }

        }
    }

    private void initAlias() {
        for (AliasFlag aliasFlag : aliasMap.keySet()) {
            // 保存并生成短别名
            this.suggestAlias(aliasFlag, aliasFlag.getSuggestAlias());
        }
    }

    public void forceAlias(AliasFlag aliasFlag, String forceAlias) {
        // 强制指定的别名，这里检查下
        int repeatCount = repeatAlias.compute(forceAlias, (k, v) -> {
            if (v == null) {
                return 0;
            }
            return v + 1;
        });
        if (repeatCount > 0) {
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

        if (repeatCount == 0 && this.aliasMap.get(aliasFlag) == null) {
            this.aliasMap.put(aliasFlag, alias);
        } else {
            this.aliasMap.put(aliasFlag, alias + '_' + repeatCount);
        }

        String shortAlias = this.generateShortAlias(shortAliasMap);
        shortAliasMap.put(aliasFlag, shortAlias);
    }


    /**
     * Description 生成短别名的规则
     *
     * @author zhaolaiyuan
     * Date 2022/10/6 11:30
     **/
    private String generateShortAlias(Map<AliasFlag, String> shortAliasMap) {
        int tableCount = shortAliasMap.size();
        String shortName;
        if (tableCount < 26) {
            shortName = String.valueOf((char) (tableCount + 'a'));
        } else {
            // 最高支持一个查询里有 26*26 个表
            int aa = tableCount / 26;
            int bb = tableCount % 26;
            shortName = "" + (char) aa + (char) bb;
        }
        return shortName;

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

    public SQLAliasManager.Copier copier() {
        return new SQLAliasManager.Copier(aliasMap, shortAliasMap);
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


    public class Copier {

        private final Map<AliasFlag, String> aliasMap;

        private final Map<AliasFlag, String> shortAliasMap;

        private Copier(Map<AliasFlag, String> aliasMap, Map<AliasFlag, String> shortAliasMap) {
            this.aliasMap = aliasMap;
            this.shortAliasMap = shortAliasMap;
        }


        /**
         * Description 删掉某个别名，通常是为了做一些优化，让语句好看一点
         *
         * @author zhaolaiyuan
         * Date 2022/12/18 12:08
         **/
        public void removeAlias(AliasFlag aliasFlag) {
            if (aliasFlag == null) {
                return;
            }
            shortAliasMap.remove(aliasFlag);
            aliasMap.remove(aliasFlag);
        }

        public void putAlias(AliasFlag aliasFlag, String alias) {
            this.aliasMap.put(aliasFlag, alias);
        }

        public void putShortAlias(AliasFlag aliasFlag, String alias) {
            this.aliasMap.put(aliasFlag, alias);
        }

        public SQLAliasManager copy() {
            return new SQLAliasManager(configProperties, aliasMap, shortAliasMap);
        }
    }
}
