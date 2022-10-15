package zly.rivulet.sql.definition.update;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.field.FieldDefinition;
import zly.rivulet.sql.definition.query.SQLBlueprint;
import zly.rivulet.sql.definition.query.main.FromDefinition;
import zly.rivulet.sql.definition.query.main.WhereDefinition;
import zly.rivulet.sql.definition.query.operate.AndOperateDefinition;
import zly.rivulet.sql.definition.query.operate.EqOperateDefinition;
import zly.rivulet.sql.describer.condition.ConditionContainer;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.describer.update.SqlUpdateMetaDesc;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class SqlUpdateDefinition implements SQLBlueprint {

    private final RivuletFlag flag = RivuletFlag.UPDATE;

    private FromDefinition fromDefinition;

    private SetDefinition setDefinition;

    private WhereDefinition whereDefinition;

    private SQLAliasManager aliasManager;

    private ParamReceiptManager paramReceiptManager;

    public SqlUpdateDefinition(SqlParserPortableToolbox toolbox, WholeDesc wholeDesc) {
        SqlUpdateMetaDesc<?> metaDesc = (SqlUpdateMetaDesc<?>) wholeDesc;
        Class<?> mainFrom = metaDesc.getMainFrom();
        if (ClassUtils.isExtend(QueryComplexModel.class, mainFrom)) {
            // 仅支持单表更新
            throw SQLDescDefineException.unSupportMultiModelUpdate();
        }
        QueryProxyNode queryProxyNode = new QueryProxyNode(toolbox, mainFrom);
        toolbox.setCurrNode(queryProxyNode);

        this.fromDefinition = new FromDefinition(toolbox, metaDesc);

        this.setDefinition = new SetDefinition(toolbox, metaDesc.getMappedItemList());

        ConditionContainer<?, ?> whereConditionContainer = metaDesc.getWhereConditionContainer();
        if (whereConditionContainer != null) {
            this.whereDefinition = new WhereDefinition(toolbox, whereConditionContainer);
        }

        this.aliasManager = SQLAliasManager.create(toolbox.getConfigProperties(), queryProxyNode);
        this.paramReceiptManager = toolbox.getParamReceiptManager();

    }

    public SqlUpdateDefinition(SqlParserPortableToolbox toolbox, SQLModelMeta sqlModelMeta, SQLFieldMeta primaryKey) {
        Class<?> mainFrom = sqlModelMeta.getModelClass();
        if (ClassUtils.isExtend(QueryComplexModel.class, mainFrom)) {
            // 仅支持单表更新
            throw SQLDescDefineException.unSupportMultiModelUpdate();
        }
        QueryProxyNode queryProxyNode = new QueryProxyNode(toolbox, mainFrom);
        toolbox.setCurrNode(queryProxyNode);

        this.fromDefinition = new FromDefinition(toolbox, metaDesc);

        this.setDefinition = new SetDefinition(toolbox, sqlModelMeta);

        Param<? extends SQLFieldMeta> mainIdParam = Param.of(primaryKey.getClass(), Constant.MAIN_ID, SqlParamCheckType.NATURE);
        Param<? extends SQLFieldMeta> mainIdsParam = Param.of(primaryKey.getClass(), Constant.MAIN_IDS, SqlParamCheckType.NATURE);
        this.whereDefinition = new WhereDefinition(
            toolbox,
            new AndOperateDefinition(
                toolbox,
                new EqOperateDefinition(
                    toolbox,
                    new FieldDefinition(null, sqlModelMeta, primaryKey),
                    mainIdParam,
                    CheckCondition.notNull(mainIdParam)

                )
            )
        );

        this.aliasManager = SQLAliasManager.create(toolbox.getConfigProperties(), queryProxyNode);
        this.paramReceiptManager = toolbox.getParamReceiptManager();
    }

    @Override
    public RivuletFlag getFlag() {
        return flag;
    }

    @Override
    public Assigner<?> getAssigner() {
        return null;
    }

    @Override
    public ParamReceiptManager getParamReceiptManager() {
        return this.paramReceiptManager;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }

    @Override
    public SQLAliasManager getAliasManager() {
        return this.aliasManager;
    }

    public FromDefinition getFromDefinition() {
        return fromDefinition;
    }

    public SetDefinition getSetDefinition() {
        return setDefinition;
    }

    public WhereDefinition getWhereDefinition() {
        return whereDefinition;
    }
}