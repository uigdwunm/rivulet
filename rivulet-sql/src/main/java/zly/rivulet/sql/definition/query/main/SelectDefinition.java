package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractContainerDefinition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.mapper.MapDefinition;
import zly.rivulet.sql.describer.query.desc.Mapping;
import zly.rivulet.sql.preparser.SqlPreParseHelper;

import java.util.ArrayList;
import java.util.List;

public class SelectDefinition extends AbstractContainerDefinition {

    private final Class<?> selectModel;

    private final List<? extends Mapping.Item<?, ?, ?>> mappedItemList;

    private final boolean nameMapped;

//    protected SelectDefinition() {
//        super(CheckCondition.IS_TRUE);
//    }

    public SelectDefinition(SqlPreParseHelper sqlPreParseHelper, FromDefinition fromDefinition, Class<?> selectModel, List<? extends Mapping.Item<?, ?, ?>> mappedItemList, boolean nameMapped) {
        super(CheckCondition.IS_TRUE, sqlPreParseHelper.getSqlParamDefinitionManager());
        this.selectModel = selectModel;
        this.mappedItemList = mappedItemList;
        this.nameMapped = nameMapped;
    }

    public MapDefinition getMapDefinition() {
        MapDefinition mapDefinition = new MapDefinition();

        // TODO
        return mapDefinition;
    }

    @Override
    public SelectDefinition forAnalyze() {
        return null;
    }

    @Override
    public ArrayList<?> getList() {
        return null;
    }
}