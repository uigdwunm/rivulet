package zly.rivulet.sql.mapper;

import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;
import zly.rivulet.sql.preparser.helper.node.FromNode;
import zly.rivulet.sql.preparser.helper.node.ModelProxyNode;
import zly.rivulet.sql.preparser.helper.node.QueryProxyNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ContainerAssigner extends Assigner {

    private final View<Assigner> assignerList;

    private int size = 0;

    public ContainerAssigner(
        SqlPreParseHelper sqlPreParseHelper,
        QueryProxyNode queryProxyNode,
        int indexStart
    ) {
        super(queryProxyNode.getFromModelClass(), indexStart);
        List<Assigner> assignerList = new ArrayList<>();
        QueryProxyNode currNode = sqlPreParseHelper.getCurrNode();

        for (FromNode subFromNode : queryProxyNode.getFromNodeList()) {
            Assigner assigner;
            if (subFromNode instanceof ModelProxyNode) {
                ModelProxyNode modelProxyNode = (ModelProxyNode) subFromNode;
                assigner = new ModelAssigner(modelProxyNode, indexStart);
            } else if (subFromNode instanceof QueryProxyNode) {
                QueryProxyNode subQueryProxyNode = (QueryProxyNode) subFromNode;
                if (subQueryProxyNode.getFromModelClass().equals(subQueryProxyNode.getSelectModelClass())) {
                    SqlMapDefinition subMapDefinition = subQueryProxyNode.getSqlQueryDefinition().getMapDefinition();
                    assigner = subMapDefinition.getAssigner();
                } else {
                    assigner = new ContainerAssigner(sqlPreParseHelper, subQueryProxyNode, indexStart);
                }
            } else {
                throw UnbelievableException.unknownType();
            }

            // 需要把自己塞到父类中
            Field field = queryProxyNode.getField(currNode);
            field.setAccessible(true);
            assigner.setAssigner((outerContainer, o) -> {
                try {
                    field.set(outerContainer, o);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            indexStart += assigner.size();
            this.size += assigner.size();
            assignerList.add(assigner);
            currNode.addMappingDefinitionList(subFromNode.getMappingDefinitionList());
        }
        this.assignerList = View.create(assignerList);
    }


    @Override
    public Object assign(List<Object> resultValues) {
        Object o = super.containerCreator.get();

        for (Assigner subAssigner : assignerList) {
            subAssigner.assign(o, resultValues);
        }
        return o;
    }

    @Override
    public int size() {
        return size;
    }


}
