package zly.rivulet.sql.assigner;

import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;
import zly.rivulet.sql.parser.node.FromNode;
import zly.rivulet.sql.parser.node.ModelProxyNode;
import zly.rivulet.sql.parser.node.QueryProxyNode;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ContainerSQLAssigner extends SQLAssigner {

    private final View<SQLAssigner> assignerList;

    private int size = 0;

    public ContainerSQLAssigner(
        SqlParserPortableToolbox sqlPreParseHelper,
        QueryProxyNode queryProxyNode,
        int indexStart
    ) {
        super(queryProxyNode.getFromModelClass(), indexStart);
        List<SQLAssigner> SQLAssignerList = new ArrayList<>();
        QueryProxyNode currNode = sqlPreParseHelper.getCurrNode();

        for (FromNode subFromNode : queryProxyNode.getFromNodeList()) {
            SQLAssigner sqlAssigner;
            if (subFromNode instanceof ModelProxyNode) {
                ModelProxyNode modelProxyNode = (ModelProxyNode) subFromNode;
                sqlAssigner = new ModelSQLAssigner(modelProxyNode, indexStart);
            } else if (subFromNode instanceof QueryProxyNode) {
                QueryProxyNode subQueryProxyNode = (QueryProxyNode) subFromNode;
                if (subQueryProxyNode.getFromModelClass().equals(subQueryProxyNode.getSelectModelClass())) {
                    sqlAssigner = subQueryProxyNode.getSqlQueryDefinition().getAssigner();
                } else {
                    sqlAssigner = new ContainerSQLAssigner(sqlPreParseHelper, subQueryProxyNode, indexStart);
                }
            } else {
                throw UnbelievableException.unknownType();
            }

            // 需要把自己塞到父类中
            Field field = queryProxyNode.getField(currNode);
            field.setAccessible(true);
            sqlAssigner.setAssigner((outerContainer, o) -> {
                try {
                    field.set(outerContainer, o);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            indexStart += sqlAssigner.size();
            this.size += sqlAssigner.size();
            SQLAssignerList.add(sqlAssigner);
            currNode.addMappingDefinitionList(subFromNode.getMapDefinitionList());
        }
        this.assignerList = View.create(SQLAssignerList);
    }


    @Override
    public Object assign(ResultSet resultSet) {
        Object o = super.buildContainer();

        for (SQLAssigner subSQLAssigner : assignerList) {
            subSQLAssigner.assign(o, resultSet);
        }
        return o;
    }

    @Override
    public int size() {
        return size;
    }


}
