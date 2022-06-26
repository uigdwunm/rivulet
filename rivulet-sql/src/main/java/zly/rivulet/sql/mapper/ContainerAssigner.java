package zly.rivulet.sql.mapper;

import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definition.query.mapping.MappingDefinition;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;
import zly.rivulet.sql.preparser.helper.node.FromNode;
import zly.rivulet.sql.preparser.helper.node.QueryProxyNode;

import java.util.ArrayList;
import java.util.List;

public class ContainerAssigner extends Assigner {

    private final View<Assigner> assignerList;

    public ContainerAssigner(
        SqlPreParseHelper sqlPreParseHelper,
        QueryProxyNode queryProxyNode,
        List<MappingDefinition> mappingDefinitionList,
        int indexStart
    ) {
        super(queryProxyNode, indexStart);
        List<Assigner> assignerList = new ArrayList<>();

        this.assignerList = View.create(assignerList);

        for (FromNode subFromNode : queryProxyNode.getFromNodeList()) {
            Assigner subAssigner = Assigner.createAssigner(sqlPreParseHelper, subFromNode, mappingDefinitionList);
            indexStart +=
            assignerList.add(subAssigner);
        }
    }


    @Override
    public Object assign(List<Object> resultValues) {
        Object o = containerCreator.get();

        for (Assigner subAssigner : assignerItemList) {
            subAssigner.assign(o, resultValues);
        }
        return o;
    }

}
