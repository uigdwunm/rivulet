package zly.rivulet.sql.definition.update;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.parser.param.ParamDefinitionManager;
import zly.rivulet.sql.definition.query.SQLBlueprint;
import zly.rivulet.sql.definition.query.main.FromDefinition;
import zly.rivulet.sql.definition.query.main.WhereDefinition;
import zly.rivulet.sql.describer.condition.ConditionContainer;
import zly.rivulet.sql.describer.update.SqlUpdateMetaDesc;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.ArrayList;
import java.util.List;

public class SqlUpdateDefinition implements SQLBlueprint {
    private final SqlUpdateMetaDesc<?> metaDesc;

    private FromDefinition fromDefinition;

    private SetDefinition setDefinition;

    private WhereDefinition whereDefinition;

    private final List<AbstractDefinition> subDefinitionList = new ArrayList<>();

    private SQLAliasManager aliasManager;

    private ParamDefinitionManager paramDefinitionManager;

    public SqlUpdateDefinition(SqlParserPortableToolbox toolbox, WholeDesc wholeDesc) {
        SqlUpdateMetaDesc<?> metaDesc = (SqlUpdateMetaDesc<?>) wholeDesc;
        QueryProxyNode queryProxyNode = new QueryProxyNode(toolbox, metaDesc.getMainFrom());
        toolbox.setCurrNode(queryProxyNode);

        this.metaDesc = metaDesc;

        this.fromDefinition = new FromDefinition(toolbox);
        this.setDefinition = new SetDefinition(toolbox, metaDesc.getMappedItemList());

        this.subDefinitionList.add(this.fromDefinition);
        this.subDefinitionList.add(this.setDefinition);

        ConditionContainer<?, ?> whereConditionContainer = metaDesc.getWhereConditionContainer();
        if (whereConditionContainer != null) {
            this.whereDefinition = new WhereDefinition(toolbox, whereConditionContainer);
            this.subDefinitionList.add(this.whereDefinition);
        }

        this.aliasManager = SQLAliasManager.create(toolbox.getConfigProperties(), queryProxyNode);
        this.paramDefinitionManager = toolbox.getParamDefinitionManager();

    }

    @Override
    public Assigner<?> getAssigner() {
        return null;
    }

    @Override
    public ParamDefinitionManager getParamDefinitionManager() {
        return null;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }

    @Override
    public SQLAliasManager getAliasManager() {
        return null;
    }
}