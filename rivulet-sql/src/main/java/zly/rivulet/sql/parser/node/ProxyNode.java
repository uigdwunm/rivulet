package zly.rivulet.sql.parser.node;

import zly.rivulet.sql.parser.SQLAliasManager;

/**
 * Description sql语句存在多种嵌套子查询的可能，需要一种数据结构记录过程中的产物数据，用于管理过程中产生的代理对象和别名
 * 所以节点包括两种，from的表、select的字段
 *
 * @author zhaolaiyuan
 * Date 2022/6/12 13:27
 **/
public interface ProxyNode {

    SQLAliasManager.AliasFlag getAliasFlag();

}
