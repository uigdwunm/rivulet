package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractContainerDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.proxy_node.CommonSelectNode;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

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


    public SelectDefinition(SqlParserPortableToolbox toolbox, Class<?> selectModel, QueryProxyNode queryProxyNode) {
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

    @Override
    public SelectDefinition forAnalyze() {
        return null;
    }

    public View<MapDefinition> getMapDefinitionList() {
        return mappingDefinitionList;
    }

    public Class<?> getSelectModel() {
        return selectModel;
    }
}