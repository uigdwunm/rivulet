package zly.rivulet.sql.mapper;

import zly.rivulet.base.describer.field.SelectMapping;
import zly.rivulet.sql.definition.query.mapping.MappingDefinition;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;
import zly.rivulet.sql.preparser.helper.node.FromNode;
import zly.rivulet.sql.preparser.helper.node.ModelProxyNode;
import zly.rivulet.sql.preparser.helper.node.QueryProxyNode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Supplier;

public abstract class Assigner {
    /**
     * 把自己塞到父对象的assigner
     **/
    private final SelectMapping<Object, Object> assigner;

    private final Supplier<?> containerCreator;

    /**
     * 在select中的位置索引起始
     **/
    private final int indexStart;

    protected Assigner(FromNode fromNode, int indexStart) {
        QueryProxyNode parentNode = fromNode.getParentNode();
        Class<?> modelClass = fromNode.getModelClass();
        if (parentNode != null) {
            Field field = parentNode.getField(fromNode);
            this.assigner = (outerContainer, o) -> {
                try {
                    field.set(outerContainer, o);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            };

        } else {
            this.assigner = null;
        }
        this.containerCreator = this.buildContainerCreator(modelClass);
        this.indexStart = indexStart;
    }

    private Supplier<?> buildContainerCreator(Class<?> modelClass) {
        try {
            Constructor<?> constructor = modelClass.getConstructor();
            constructor.setAccessible(true);
            return () -> {
                try {
                    return constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (NoSuchMethodException e) {
            throw
        }
    }

    public Object assign(Object outerContainer, List<Object> resultValues) {
        Object o = this.assign(resultValues);
        assigner.setMapping(containerCreator, o);
        return o;
    }

    public abstract Object assign(List<Object> resultValues);

    public static Assigner createAssigner(SqlPreParseHelper sqlPreParseHelper, FromNode fromNode, List<MappingDefinition> mappingDefinitionList) {

        if (fromNode instanceof QueryProxyNode) {
            return new ContainerAssigner(sqlPreParseHelper, (QueryProxyNode) fromNode, mappingDefinitionList);
        } else if (fromNode instanceof ModelProxyNode) {
            ModelProxyNode modelProxyNode = (ModelProxyNode) fromNode;
            // TODO
        }
    }
}
