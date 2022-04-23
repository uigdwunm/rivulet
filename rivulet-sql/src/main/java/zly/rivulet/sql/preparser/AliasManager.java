package zly.rivulet.sql.preparser;

import zly.rivulet.sql.definer.meta.QueryFromMeta;

import java.util.*;

/**
 * Description 别名管理器，
 * 查询语句中sql表的别名不能重复，这里检查，每个查询definition都会生成一个
 *
 * 别名有三种方式：
 *  指定别名
 *
 * @author zhaolaiyuan
 * Date 2022/4/4 13:57
 **/
public class AliasManager {

    private final Map<QueryFromMeta, String> meta_alias_map = new HashMap<>();

    /**
     * 短别名映射
     **/
    private final Map<QueryFromMeta, String> meta_shortAlias_map = new HashMap<>();

    private final Map<String, QueryFromMeta> alias_meta_map = new HashMap<>();

    private final Deque<Location> locationDeque = new LinkedList<>();

    // 记录当前层
    private QueryFromMeta curr;

    // 是否返回简化别名
    private boolean isShortAlias;

    public AliasManager(boolean isShortAlias) {
        this.isShortAlias = isShortAlias;
    }

    /**
     * 会遇到一些情况，导致无法再使用简化别名（比如有一段自己拼接的语句）
     **/
    public void useShortAlias() {
        isShortAlias = false;
    }


    public String getAlias(QueryFromMeta queryFromMeta) {
        String alias = meta_alias_map.get(queryFromMeta);
        if (alias != null) {
            return alias;
        }
        // 获取注解，可能没有
        queryFromMeta.getAliasAnnotation();
        // 获取备用的别名，就是对象名
        queryFromMeta.getbackUpAlias();

        QueryFromMeta value = alias_meta_map.get(alias);
        if (value != null) {
            // 重复了
            throw new
        }

        this.curr = queryFromMeta;


    }

    /**
     * Description 进入了一层子查询
     *
     * @author zhaolaiyuan
     * Date 2022/4/5 15:24
     **/
    public void startSub() {
        if (curr == null) {
            // 不是子查询，是总查询
            return;
        }
        locationDeque.addLast(new Location(meta_alias_map.get(curr)));
    }

    public void endSub() {
        if (locationDeque.size() > 0) {
            locationDeque.removeLast();
        }
    }

    /**
     * Description 定位器，可以知道当前是位于第几层子查询中
     *
     * @author zhaolaiyuan
     * Date 2022/4/5 14:10
     **/
    private static class Location {
        private int index = 0;

        public final String prefix;

        private Location(String prefix) {
            this.prefix = prefix;
        }

        private String getShortAlias() {
            int index = this.index;
            int i = index / 26;
            index = index % 26;
            char c = (char) ('a' + index);
            if (i > 0) {
                return "" + c + i;
            } else {
                return "" + c;
            }
        }
    }

}
