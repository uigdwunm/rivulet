package zly.rivulet.sql.preparser.helper.node;

import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.preparser.SQLAliasManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Description sql语句存在多种嵌套子查询的可能，需要一种数据结构记录过程中的产物数据，用于管理过程中产生的代理对象和别名
 * 所以节点包括两种，from的表、select的字段
 *
 * @author zhaolaiyuan
 * Date 2022/6/12 13:27
 **/
public interface ProxyNode {

    SQLAliasManager.AliasFlag getAliasFlag();

    ProxyNode getParentNode();

}
