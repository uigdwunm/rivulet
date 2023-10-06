package zly.rivulet.sql.definition.query_.main;

import zly.rivulet.base.definition.AbstractContainerDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definition.query_.mapping.MapDefinition;
import zly.rivulet.sql.parser.proxy_node.CommonSelectNode;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox_.SQLParserPortableToolbox;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 * 有两种情况，
 * 1，传了mappedItemList，则按照传的走。
 * 2，没传，返回from对象的
 *
 * @author zhaolaiyuan
 * Date 2022/6/5 20:12
 **/
public class SelectDefinition extends AbstractContainerDefinition {

    private final Class<?> selectModel;

    private final View<MapDefinition> mappingDefinitionList;

    private SelectDefinition(CheckCondition checkCondition, Class<?> selectModel, View<MapDefinition> mappingDefinitionList) {
        super(checkCondition, null);
        this.selectModel = selectModel;
        this.mappingDefinitionList = mappingDefinitionList;
    }

    public SelectDefinition(SQLParserPortableToolbox toolbox, Class<?> selectModel, QueryProxyNode queryProxyNode) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());
        List<MapDefinition> mapDefinitionList = queryProxyNode.getSelectNodeList().stream()
            .map(selectNode -> {
                if (selectNode instanceof QueryProxyNode) {
                    // 子查询，包裹成MapDefinition
                    return new MapDefinition(selectNode.getQuerySelectMeta(), null, selectNode.getAliasFlag());
                } else if (selectNode instanceof CommonSelectNode) {
                    return (MapDefinition) selectNode.getQuerySelectMeta();
                } else {
                    throw UnbelievableException.unknownType();
                }
            }).collect(Collectors.toList());
        this.selectModel = selectModel;
        this.mappingDefinitionList = View.create(mapDefinitionList);

    }

    public View<MapDefinition> getMapDefinitionList() {
        return mappingDefinitionList;
    }

    public Class<?> getSelectModel() {
        return selectModel;
    }

    @Override
    public Copier copier() {
        return new Copier(selectModel, mappingDefinitionList);
    }

    public class Copier implements Definition.Copier {

        private Class<?> selectModel;

        private View<MapDefinition> mappingDefinitionList;

        private Copier(Class<?> selectModel, View<MapDefinition> mappingDefinitionList) {
            this.selectModel = selectModel;
            this.mappingDefinitionList = mappingDefinitionList;
        }

        public void setSelectModel(Class<?> selectModel) {
            this.selectModel = selectModel;
        }

        public void setMappingDefinitionList(View<MapDefinition> mappingDefinitionList) {
            this.mappingDefinitionList = mappingDefinitionList;
        }

        @Override
        public SelectDefinition copy() {
            return new SelectDefinition(checkCondition, selectModel, mappingDefinitionList);
        }
    }
}