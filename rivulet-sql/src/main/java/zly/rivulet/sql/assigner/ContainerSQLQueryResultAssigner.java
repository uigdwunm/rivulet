package zly.rivulet.sql.assigner;

import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;
import zly.rivulet.sql.parser.node.FromNode;
import zly.rivulet.sql.parser.node.ModelProxyNode;
import zly.rivulet.sql.parser.node.QueryProxyNode;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ContainerSQLQueryResultAssigner extends SQLQueryResultAssigner {

    private final View<SQLQueryResultAssigner> assignerList;

    private int size = 0;

    public ContainerSQLQueryResultAssigner(
        SqlParserPortableToolbox sqlPreParseHelper,
        QueryProxyNode queryProxyNode,
        int indexStart
    ) {
        super(queryProxyNode.getFromModelClass(), indexStart);
        List<SQLQueryResultAssigner> SQLQueryResultAssignerList = new ArrayList<>();
        QueryProxyNode currNode = sqlPreParseHelper.getCurrNode();

        for (FromNode subFromNode : queryProxyNode.getFromNodeList()) {
            SQLQueryResultAssigner sqlQueryResultAssigner;
            if (subFromNode instanceof ModelProxyNode) {
                // from 是表
                ModelProxyNode modelProxyNode = (ModelProxyNode) subFromNode;
                sqlQueryResultAssigner = new ModelSQLQueryResultAssigner(modelProxyNode, indexStart);
            } else if (subFromNode instanceof QueryProxyNode) {
                // from 是子查询
                QueryProxyNode subQueryProxyNode = (QueryProxyNode) subFromNode;
                SqlQueryDefinition sqlQueryDefinition = subQueryProxyNode.getSqlQueryDefinition();
                if (subQueryProxyNode.getFromModelClass().equals(sqlQueryDefinition.getSelectDefinition().getSelectModel())) {
                    sqlQueryResultAssigner = sqlQueryDefinition.getSelectDefinition().getSqlAssigner();
                } else {
                    sqlQueryResultAssigner = new ContainerSQLQueryResultAssigner(sqlPreParseHelper, subQueryProxyNode, indexStart);
                }
            } else {
                throw UnbelievableException.unknownType();
            }

            // 需要把自己塞到父类中
            Field field = queryProxyNode.getField(currNode);
            field.setAccessible(true);
            sqlQueryResultAssigner.setAssigner((outerContainer, o) -> {
                try {
                    field.set(outerContainer, o);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            indexStart += sqlQueryResultAssigner.size();
            this.size += sqlQueryResultAssigner.size();
            SQLQueryResultAssignerList.add(sqlQueryResultAssigner);
            currNode.addMappingDefinitionList(subFromNode.getMapDefinitionList());
        }
        this.assignerList = View.create(SQLQueryResultAssignerList);
    }


    @Override
    public Object assign(ResultSet resultSet) {
        Object o = super.buildContainer();

        for (SQLQueryResultAssigner subSQLQueryResultAssigner : assignerList) {
            subSQLQueryResultAssigner.assign(o, resultSet);
        }
        return o;
    }

    @Override
    public int size() {
        return size;
    }


}
