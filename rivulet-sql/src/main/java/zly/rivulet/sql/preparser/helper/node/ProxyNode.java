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
public class ProxyNode {
    /**
     * 当前节点所属的父节点,如果是根节点则为null
     **/
    protected final ProxyNode parentNode;

    /**
     * 结果对象创建，传入参数list用于收集过程中生成的赋值器
     **/
    protected Function<List<Consumer<Object>>, Object> creator;

    /**
     * 当前节点关联的子节点
     **/
    protected final List<ProxyNode> childProxyNodeList = new ArrayList<>();

    /**
     * 当前对象的代理对象，如果不是对象则为空
     *
     **/
    private Object proxyModel;

    /**
     * 当前对象的meta，如果不是对象则为空
     **/
    private QueryFromMeta queryFromMeta;

    /**
     * 当前节点的别名flag
     **/
    private final SQLAliasManager.AliasFlag aliasFlag;

    /**
     * 当前节点下存储别名的map
     **/
    private final Map<SQLAliasManager.AliasFlag, String> aliasMap = new HashMap<>();

    /**
     * 当前节点下存储短别名的map
     **/
    private final Map<SQLAliasManager.AliasFlag, String> shortAliasMap = new HashMap<>();

    private ProxyNode() {
        this.parentNode = null;
        this.aliasFlag = null;
    }

    public static ProxyNode craeteRoot() {
        return new ProxyNode();
    }


    public ProxyNode getParentNode() {
        return parentNode;
    }

    public Object getProxyModel() {
        return proxyModel;
    }

    public void setQueryFromMeta(QueryFromMeta queryFromMeta) {
        this.queryFromMeta = queryFromMeta;
    }

    public void setProxyModel(Object proxyModel) {
        this.proxyModel = proxyModel;
    }

    /**
     * Description 合并子查询的node数据
     *
     * @author zhaolaiyuan
     * Date 2022/6/19 12:13
     **/
    public void addSubNode(ProxyNode subNode) {
        // TODO
    }
}
