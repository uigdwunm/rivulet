package zly.rivulet.sql.parser;

import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.sql.SQLRivuletProperties;
import zly.rivulet.sql.exception.SQLDescDefineException;

import java.util.HashMap;
import java.util.Map;

public class SQLAliasManager {

    private final SQLRivuletProperties configProperties;

    private final Map<Object, String> aliasMap;

    private final Map<Object, String> shortAliasMap;

    /**
     * 检测用，所有别名全部加载后丢弃
     **/
    private final Map<String, Integer> repeatAlias;

    public SQLAliasManager(SQLRivuletProperties configProperties) {
        this(configProperties, new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    private SQLAliasManager(
            SQLRivuletProperties configProperties,
            Map<Object, String> aliasMap,
            Map<Object, String> shortAliasMap,
            Map<String, Integer> repeatAlias
) {
        this.configProperties = configProperties;
        this.aliasMap = aliasMap;
        this.shortAliasMap = shortAliasMap;
        this.repeatAlias = repeatAlias;
    }

    /**
     * 强制指定别名，冲突会报错
     **/
    public void forceAlias(Object aliasFlag, String forceAlias) {
        String s = aliasMap.get(aliasFlag);
        if (s != null) {
            // 已经注册过
            if (forceAlias.equals(s)) {
                // 强制指定的和当前的一致，不用管
                return;
            } else {
                throw SQLDescDefineException.forceAliasRepeat(forceAlias, s);
            }
        }
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

    /**
     * 建议别名，冲突会加数字_
     **/
    public void suggestAlias(Object aliasFlag, String alias) {
        if (aliasMap.get(aliasFlag) != null) {
            // 已经注册过，直接返回
            return;
        }
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
    private String generateShortAlias(Map<Object, String> shortAliasMap) {
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
    public String getAlias(Object aliasFlag) {
        if (aliasFlag == null) {
            return null;
        }
        if (configProperties.isUseShortAlias()) {
            return shortAliasMap.get(aliasFlag);
        } else {
            return aliasMap.get(aliasFlag);
        }
    }

    public SQLAliasManager.Copier copier() {
        return new Copier(
                configProperties,
                new HashMap<>(aliasMap),
                new HashMap<>(shortAliasMap),
                new HashMap<>(repeatAlias)
        );
    }

    public static class Copier {

        private final SQLRivuletProperties configProperties;

        private Map<Object, String> aliasMap;

        private Map<Object, String> shortAliasMap;

        /**
         * 计数用
         **/
        private Map<String, Integer> repeatAlias;

        private Copier(
                SQLRivuletProperties configProperties,
                Map<Object, String> aliasMap,
                Map<Object, String> shortAliasMap,
                Map<String, Integer> repeatAlias
        ) {
            this.configProperties = configProperties;
            this.aliasMap = aliasMap;
            this.shortAliasMap = shortAliasMap;
            this.repeatAlias = repeatAlias;
        }


        /**
         * Description 删掉某个别名，通常是为了做一些优化，让语句好看一点
         *
         * @author zhaolaiyuan
         * Date 2022/12/18 12:08
         **/
        public void removeAlias(Object aliasFlag) {
            if (aliasFlag == null) {
                return;
            }
            shortAliasMap.remove(aliasFlag);
            aliasMap.remove(aliasFlag);
//            repeatAlias
        }

        public void putAlias(Object aliasFlag, String alias) {
            this.aliasMap.put(aliasFlag, alias);
        }

        public void putShortAlias(Object aliasFlag, String alias) {
            this.aliasMap.put(aliasFlag, alias);
        }

        public SQLAliasManager copy() {
            return new SQLAliasManager(configProperties, aliasMap, shortAliasMap, repeatAlias);
        }
    }
}
