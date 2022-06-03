package zly.rivulet.sql.preparser;

import zly.rivulet.base.exception.UnbelievableException;

import java.util.*;

public class SQLAliasManager {

    private Map<AliasFlag, String> aliasMap = new HashMap<>();

    private Map<AliasFlag, String> shortAliasMap = new HashMap<>();

    private Set<String> aliasSet = new HashSet<>();

    // 记录每个层级表数量
    private List<Integer> indexRecord = new ArrayList<>();

    private int index;

    /**
     * 使用短别名
     **/
    private final boolean useShortAlias;

    public SQLAliasManager(boolean useShortAlias) {
        this.useShortAlias = useShortAlias;
    }

    public AliasFlag createFlag(String alias) {
        AliasFlag aliasFlag = new AliasFlag();
        aliasMap.put(aliasFlag, alias);
        // TODO
        Integer record = indexRecord.get(index);
        shortAliasMap.put(aliasFlag, ((char) (index + 'a') + "" + record));
        indexRecord.set(index, record + 1);
        return aliasFlag;
    }

    /**
     * Description 进入了一层子查询
     *
     * @author zhaolaiyuan
     * Date 2022/4/5 15:24
     **/
    public void startSub() {
        index++;

        if (indexRecord.size() < index) {
            indexRecord.add(0);
        }
    }

    public void endSub() {
        index--;
    }

    /**
     * Description 用flag来换取别名
     *
     * @author zhaolaiyuan
     * Date 2022/5/14 15:45
     **/
    public String getAlias(AliasFlag aliasFlag) {
        if (useShortAlias) {
            return shortAliasMap.get(aliasFlag);
        } else {
            return aliasMap.get(aliasFlag);
        }

    }

    public void merge(SQLAliasManager subAliasManager) {

    }

    /**
     * Description 一个独立查询对象的唯一标识
     *
     * @author zhaolaiyuan
     * Date 2022/5/14 11:30
     **/
    public static class AliasFlag {

        private AliasFlag() {}
    }
}
