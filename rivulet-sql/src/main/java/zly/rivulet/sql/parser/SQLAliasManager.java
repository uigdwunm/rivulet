package zly.rivulet.sql.parser;

import zly.rivulet.sql.SQLRivuletProperties;
import zly.rivulet.sql.exception.SQLDescDefineException;

import java.util.HashMap;
import java.util.Map;

public class SQLAliasManager {

    private final SQLRivuletProperties configProperties;

    private final Map<AliasFlag, String> aliasMap;

    private final Map<AliasFlag, String> shortAliasMap;

    /**
     * 检测用，所有别名全部加载后丢弃
     **/
    private final Map<String, Integer> repeatAlias;

    public static class AliasFlag {
        private final String alias;

        private final boolean isForce;

        private final AliasFlag parents;

        private AliasFlag(String alias, boolean isForce, AliasFlag parents) {
            this.alias = alias;
            this.isForce = isForce;
            this.parents = parents;
        }
    }

    public SQLAliasManager(SQLRivuletProperties configProperties) {
        this.configProperties = configProperties;
        this.aliasMap = new HashMap<>();
        this.shortAliasMap = new HashMap<>();
        this.repeatAlias = new HashMap<>();
    }

    public static AliasFlag suggestAlias(String alias) {
        return suggestAlias(alias, null);
    }

    public static AliasFlag suggestAlias(String alias, AliasFlag parents) {
        return new AliasFlag(alias, false, parents);
    }

    public static AliasFlag forceAlias(String alias) {
        return forceAlias(alias, null);
    }

    public static AliasFlag forceAlias(String alias, AliasFlag parents) {
        return new AliasFlag(alias, true, parents);
    }

    public void register(AliasFlag aliasFlag) {
        String alias = aliasMap.get(aliasFlag);
        if (alias != null) {
            // 已经注册过，直接返回
            return;
        }
        alias = aliasFlag.alias;
        boolean isForce = aliasFlag.isForce;
        // 检查下重复次数
        int repeatCount = repeatAlias.compute(alias, (k, v) -> {
            if (v == null) {
                return 0;
            }
            return v + 1;
        });
        if (isForce && repeatCount > 0) {
            // 强制指定的重复了，这里报错
            throw SQLDescDefineException.forceAliasRepeat(alias);
        }

        if (repeatCount == 0) {
            this.aliasMap.put(aliasFlag, alias);
        } else {
            this.aliasMap.put(aliasFlag, alias + '_' + repeatCount);
        }

        String shortAlias = aliasFlag.isForce ? alias : this.generateShortAlias(shortAliasMap);
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
        if (aliasFlag.parents == null) {
            // 没有父级所属，那不需要别名
            return null;
        }
        if (aliasFlag.isForce) {
            return aliasFlag.alias;
        }
        if (configProperties.isUseShortAlias()) {
            return shortAliasMap.get(aliasFlag);
        } else {
            return aliasMap.get(aliasFlag);
        }
    }

//    public SQLAliasManager.Copier copier() {
//        return new Copier(
//                configProperties,
//                new HashMap<>(aliasMap),
//                new HashMap<>(shortAliasMap),
//                new HashMap<>(repeatAlias)
//        );
//    }
//
//    public static class Copier {
//
//        private final SQLRivuletProperties configProperties;
//
//        private Map<Object, String> aliasMap;
//
//        private Map<Object, String> shortAliasMap;
//
//        /**
//         * 计数用
//         **/
//        private Map<String, Integer> repeatAlias;
//
//        private Copier(
//                SQLRivuletProperties configProperties,
//                Map<Object, String> aliasMap,
//                Map<Object, String> shortAliasMap,
//                Map<String, Integer> repeatAlias
//        ) {
//            this.configProperties = configProperties;
//            this.aliasMap = aliasMap;
//            this.shortAliasMap = shortAliasMap;
//            this.repeatAlias = repeatAlias;
//        }
//
//
//        /**
//         * Description 删掉某个别名，通常是为了做一些优化，让语句好看一点
//         *
//         * @author zhaolaiyuan
//         * Date 2022/12/18 12:08
//         **/
//        public void removeAlias(Object aliasFlag) {
//            if (aliasFlag == null) {
//                return;
//            }
//            shortAliasMap.remove(aliasFlag);
//            aliasMap.remove(aliasFlag);
////            repeatAlias
//        }
//
//        public void putAlias(Object aliasFlag, String alias) {
//            this.aliasMap.put(aliasFlag, alias);
//        }
//
//        public void putShortAlias(Object aliasFlag, String alias) {
//            this.aliasMap.put(aliasFlag, alias);
//        }
//
//        public SQLAliasManager copy() {
//            return new SQLAliasManager(configProperties, aliasMap, shortAliasMap, repeatAlias);
//        }
//    }
}
