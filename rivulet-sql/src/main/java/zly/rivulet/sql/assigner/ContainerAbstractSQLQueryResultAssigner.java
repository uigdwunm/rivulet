package zly.rivulet.sql.assigner;

import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.utils.View;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;
import zly.rivulet.sql.parser.node.FromNode;
import zly.rivulet.sql.parser.node.ModelProxyNode;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ContainerAbstractSQLQueryResultAssigner extends AbstractSQLQueryResultAssigner {

    private final View<AbstractSQLQueryResultAssigner> assignerList;

    private int size = 0;

    public ContainerAbstractSQLQueryResultAssigner(
        SqlParserPortableToolbox sqlPreParseHelper,
        QueryProxyNode queryProxyNode,
        int indexStart
    ) {
        super(queryProxyNode.getFromModelClass(), indexStart);
        List<AbstractSQLQueryResultAssigner> abstractSqlQueryResultAssignerList = new ArrayList<>();
        QueryProxyNode currNode = sqlPreParseHelper.getQueryProxyNode();

        for (FromNode subFromNode : queryProxyNode.getFromNodeList()) {
            AbstractSQLQueryResultAssigner abstractSqlQueryResultAssigner;
            if (subFromNode instanceof ModelProxyNode) {
                // from 是表
                ModelProxyNode modelProxyNode = (ModelProxyNode) subFromNode;
                abstractSqlQueryResultAssigner = new ModelAbstractSQLQueryResultAssigner(modelProxyNode, indexStart);
            } else if (subFromNode instanceof QueryProxyNode) {
                // from 是子查询
                QueryProxyNode subQueryProxyNode = (QueryProxyNode) subFromNode;
                SqlQueryDefinition sqlQueryDefinition = subQueryProxyNode.getSqlQueryDefinition();
                if (subQueryProxyNode.getFromModelClass().equals(sqlQueryDefinition.getSelectDefinition().getSelectModel())) {
                    abstractSqlQueryResultAssigner = sqlQueryDefinition.getSelectDefinition().getSqlAssigner();
                } else {
                    abstractSqlQueryResultAssigner = new ContainerAbstractSQLQueryResultAssigner(sqlPreParseHelper, subQueryProxyNode, indexStart);
                }
            } else {
                throw UnbelievableException.unknownType();
            }

            // 需要把自己塞到父类中
            Field field = queryProxyNode.getField(currNode);
            field.setAccessible(true);
            abstractSqlQueryResultAssigner.setAssigner((outerContainer, o) -> {
                try {
                    field.set(outerContainer, o);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            indexStart += abstractSqlQueryResultAssigner.size();
            this.size += abstractSqlQueryResultAssigner.size();
            abstractSqlQueryResultAssignerList.add(abstractSqlQueryResultAssigner);
            currNode.addMappingDefinitionList(subFromNode.getMapDefinitionList());
        }
        this.assignerList = View.create(abstractSqlQueryResultAssignerList);
    }


    @Override
    public Object assign(ResultSet resultSet) {
        Object o = super.buildContainer();

        for (AbstractSQLQueryResultAssigner subAbstractSQLQueryResultAssigner : assignerList) {
            subAbstractSQLQueryResultAssigner.assign(o, resultSet);
        }
        return o;
    }

    @Override
    public int size() {
        return size;
    }


}
