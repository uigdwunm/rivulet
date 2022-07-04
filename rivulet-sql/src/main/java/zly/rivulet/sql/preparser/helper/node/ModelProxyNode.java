package zly.rivulet.sql.preparser.helper.node;

import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.query.mapping.MappingDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Description 单表model的代理对象节点
 *
 * @author zhaolaiyuan
 * Date 2022/6/25 9:15
 **/
public class ModelProxyNode implements FromNode {
    /**
     * 当前节点所属的父节点,如果是根节点则为null
     **/
    protected final QueryProxyNode parentNode;

    /**
     * 当前对象的代理对象，一定对应一个表对象
     *
     **/
    private final Object proxyModel;

    /**
     * 当前节点的别名flag,如果是最外层的查询，则为空
     **/
    private final SQLAliasManager.AliasFlag aliasFlag;

    private final SQLModelMeta modelMeta;

    private final List<MappingDefinition> mappingDefinitionList = new ArrayList<>();

    public ModelProxyNode(QueryProxyNode parentNode, Object proxyModel, SQLAliasManager.AliasFlag aliasFlag, SQLModelMeta modelMeta) {
        this.parentNode = parentNode;
        this.proxyModel = proxyModel;
        this.aliasFlag = aliasFlag;
        this.modelMeta = modelMeta;
    }

    @Override
    public Object getProxyModel() {
        return this.proxyModel;
    }

    @Override
    public Class<?> getFromModelClass() {
        return modelMeta.getModelClass();
    }

    @Override
    public QueryProxyNode getParentNode() {
        return this.parentNode;
    }

    @Override
    public List<MappingDefinition> getMappingDefinitionList() {
        return this.mappingDefinitionList;
    }

    public SQLModelMeta getModelMeta() {
        return modelMeta;
    }

    public SQLAliasManager.AliasFlag getAliasFlag() {
        return aliasFlag;
    }

    public void addMappingDefinition(MappingDefinition mappingDefinition) {
        this.mappingDefinitionList.add(mappingDefinition);
    }
}
