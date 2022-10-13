package zly.rivulet.sql.assigner;

import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.utils.View;

import java.sql.ResultSet;

public class ContainerAbstractSQLQueryResultAssigner extends AbstractSQLQueryResultAssigner {

    private final View<AbstractSQLQueryResultAssigner> assignerList;

    private int size;

    public ContainerAbstractSQLQueryResultAssigner(SetMapping<Object, Object> assignToParent, Class<?> modelClass, View<AbstractSQLQueryResultAssigner> assignerList) {
        super(assignToParent, modelClass);
        this.assignerList = assignerList;
        this.size = assignerList.stream().map(AbstractSQLQueryResultAssigner::size).reduce(0, Integer::sum);
    }

    public ContainerAbstractSQLQueryResultAssigner(Class<?> modelClass, View<AbstractSQLQueryResultAssigner> assignerList) {
        this(null, modelClass, assignerList);
    }

//    public ContainerAbstractSQLQueryResultAssigner(
//        SqlParserPortableToolbox sqlPreParseHelper,
//        QueryProxyNode queryProxyNode,
//        int indexStart
//    ) {
//        super(queryProxyNode.getFromModelClass(), indexStart);
//        List<AbstractSQLQueryResultAssigner> abstractSqlQueryResultAssignerList = new ArrayList<>();
//        QueryProxyNode currNode = sqlPreParseHelper.getQueryProxyNode();
//
//        for (FromNode subFromNode : queryProxyNode.getFromNodeList()) {
//            AbstractSQLQueryResultAssigner abstractSqlQueryResultAssigner;
//            if (subFromNode instanceof ModelProxyNode) {
//                // from 是表
//                ModelProxyNode modelProxyNode = (ModelProxyNode) subFromNode;
//                abstractSqlQueryResultAssigner = new ModelAbstractSQLQueryResultAssigner(modelProxyNode, indexStart);
//            } else if (subFromNode instanceof QueryProxyNode) {
//                // from 是子查询
//                QueryProxyNode subQueryProxyNode = (QueryProxyNode) subFromNode;
//                SqlQueryDefinition sqlQueryDefinition = subQueryProxyNode.getSqlQueryDefinition();
//                if (subQueryProxyNode.getFromModelClass().equals(sqlQueryDefinition.getSelectDefinition().getSelectModel())) {
//                    abstractSqlQueryResultAssigner = sqlQueryDefinition.getSelectDefinition().getSqlAssigner();
//                } else {
//                    abstractSqlQueryResultAssigner = new ContainerAbstractSQLQueryResultAssigner(sqlPreParseHelper, subQueryProxyNode, indexStart);
//                }
//            } else {
//                throw UnbelievableException.unknownType();
//            }
//
//            // 需要把自己塞到父类中
//            Field field = queryProxyNode.getField(currNode);
//            field.setAccessible(true);
//            abstractSqlQueryResultAssigner.setAssigner((outerContainer, o) -> {
//                try {
//                    field.set(outerContainer, o);
//                } catch (IllegalAccessException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//            indexStart += abstractSqlQueryResultAssigner.size();
//            this.size += abstractSqlQueryResultAssigner.size();
//            abstractSqlQueryResultAssignerList.add(abstractSqlQueryResultAssigner);
//            currNode.addMappingDefinitionList(subFromNode.getMapDefinitionList());
//        }
//        this.assignerList = View.create(abstractSqlQueryResultAssignerList);
//    }

    @Override
    public void assign(Object parentContainer, ResultSet resultSet, int indexStart) {
        for (AbstractSQLQueryResultAssigner subAssigner : assignerList) {
            Object value = subAssigner.getValue(resultSet, indexStart);
            SetMapping<Object, Object> assigner = subAssigner.getAssigner();
            // 赋值
            assigner.setMapping(parentContainer, value);
            indexStart += subAssigner.size();
        }
    }

    @Override
    public int size() {
        return size;
    }


}
