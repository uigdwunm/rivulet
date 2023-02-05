package zly.rivulet.sql.assigner;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.describer.field.SetMapping;
import zly.rivulet.base.utils.View;

import java.sql.ResultSet;
import java.util.List;

public class SQLContainerResultAssigner extends SQLQueryResultAssigner {

    /**
     * 把每个字段塞到当前model的赋值器
     **/
    private final View<ContainerFieldAssignerWrap> fieldAssignerList;
//    private final View<AbstractSQLQueryResultAssigner> assignerList;

    private int size;

    public SQLContainerResultAssigner(Class<?> modelClass, List<ContainerFieldAssignerWrap> assignerList) {
        super(modelClass);
        this.fieldAssignerList = View.create(assignerList);
        this.size = assignerList.stream().map(ContainerFieldAssignerWrap::size).reduce(0, Integer::sum);
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
        for (ContainerFieldAssignerWrap subAssignerWrap : fieldAssignerList) {
            Assigner<ResultSet> subAssigner = subAssignerWrap.assigner;
            SetMapping<Object, Object> setMapping = subAssignerWrap.setMapping;
            // 获取值
            Object value = subAssigner.getValue(resultSet, indexStart);
            // 字段赋值
            setMapping.setMapping(parentContainer, value);
            indexStart += subAssigner.size();
        }
    }

    @Override
    public int size() {
        return size;
    }

    public static class ContainerFieldAssignerWrap {
        private final Assigner<ResultSet> assigner;

        private final SetMapping<Object, Object> setMapping;

        public ContainerFieldAssignerWrap(Assigner<ResultSet> assigner, SetMapping<Object, Object> setMapping) {
            this.assigner = assigner;
            this.setMapping = setMapping;
        }
        public int size() {
            return assigner.size();
        }
    }
}
