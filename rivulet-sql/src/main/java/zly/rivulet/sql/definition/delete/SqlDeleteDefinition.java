package zly.rivulet.sql.definition.delete;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.sql.assigner.SQLUpdateResultAssigner;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.SQLBlueprint;
import zly.rivulet.sql.definition.query.main.FromDefinition;
import zly.rivulet.sql.definition.query.main.WhereDefinition;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.definition.query.operate.AndOperateDefinition;
import zly.rivulet.sql.definition.query.operate.EqOperateDefinition;
import zly.rivulet.sql.describer.condition.ConditionContainer;
import zly.rivulet.sql.describer.delete.SqlDeleteMetaDesc;
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.SqlParser;
import zly.rivulet.sql.parser.proxy_node.FromNode;
import zly.rivulet.sql.parser.proxy_node.ProxyNodeManager;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class SqlDeleteDefinition extends SQLBlueprint {

    private final FromDefinition fromDefinition;

    private final WhereDefinition whereDefinition;

    private final SQLUpdateResultAssigner assigner = new SQLUpdateResultAssigner();

    private SqlDeleteDefinition(WholeDesc wholeDesc, FromDefinition fromDefinition, WhereDefinition whereDefinition) {
        super(RivuletFlag.DELETE, wholeDesc);
        this.fromDefinition = fromDefinition;
        this.whereDefinition = whereDefinition;
    }

    public SqlDeleteDefinition(SqlParserPortableToolbox toolbox, WholeDesc wholeDesc) {
        super(RivuletFlag.DELETE, wholeDesc);
        SqlDeleteMetaDesc<?> metaDesc = (SqlDeleteMetaDesc<?>) wholeDesc;
        this.aliasManager = new SQLAliasManager(toolbox.getConfigProperties());
        Class<?> mainFrom = metaDesc.getMainFrom();
        if (ClassUtils.isExtend(QueryComplexModel.class, mainFrom)) {
            // 仅支持单表删除
            throw SQLDescDefineException.unSupportMultiModelUpdate();
        }
        // 获取QueryProxyNode
        ProxyNodeManager proxyModelManager = toolbox.getSqlPreParser().getProxyModelManager();
        QueryProxyNode queryProxyNode = proxyModelManager.getOrCreateQueryProxyNode(toolbox, metaDesc.getAnnotation(), mainFrom);
        toolbox.setQueryProxyNode(queryProxyNode);

        this.fromDefinition = new FromDefinition(toolbox);

        ConditionContainer<?, ?> whereConditionContainer = metaDesc.getWhereConditionContainer();
        if (whereConditionContainer != null) {
            this.whereDefinition = new WhereDefinition(toolbox, whereConditionContainer);
        } else {
            this.whereDefinition = null;
        }

        this.paramReceiptManager = toolbox.getParamReceiptManager();
        this.aliasManager.init(queryProxyNode);
    }

    public SqlDeleteDefinition(SqlParserPortableToolbox toolbox, SQLModelMeta sqlModelMeta, SQLFieldMeta primaryKey) {
        super(RivuletFlag.DELETE, null);
        Class<?> mainFrom = sqlModelMeta.getModelClass();
        if (ClassUtils.isExtend(QueryComplexModel.class, mainFrom)) {
            // 仅支持单表更新
            throw SQLDescDefineException.unSupportMultiModelUpdate();
        }
        SqlParser sqlParser = toolbox.getSqlPreParser();
        ProxyNodeManager proxyModelManager = sqlParser.getProxyModelManager();
        QueryProxyNode queryProxyNode = proxyModelManager.getOrCreateQueryProxyNode(toolbox, sqlModelMeta);
        toolbox.setQueryProxyNode(queryProxyNode);

        this.fromDefinition = new FromDefinition(toolbox);

        Param<? extends SQLFieldMeta> mainIdParam = Param.of(primaryKey.getClass(), Constant.MAIN_ID, SqlParamCheckType.NATURE);
        FromNode fromNode = queryProxyNode.getFromNode(0);
        this.whereDefinition = new WhereDefinition(
            toolbox,
            new AndOperateDefinition(
                toolbox,
                new EqOperateDefinition(
                    toolbox,
                    new MapDefinition(primaryKey, fromNode.getAliasFlag(), null),
                    mainIdParam,
                    CheckCondition.notNull(mainIdParam)

                )
            )
        );

        this.paramReceiptManager = toolbox.getParamReceiptManager();

        this.aliasManager = new SQLAliasManager(toolbox.getConfigProperties());
        this.aliasManager.init(queryProxyNode);
    }

    @Override
    public RivuletFlag getFlag() {
        return RivuletFlag.DELETE;
    }

    @Override
    public Assigner<?> getAssigner() {
        return this.assigner;
    }

    @Override
    public SQLAliasManager getAliasManager() {
        return this.aliasManager;
    }

    public FromDefinition getFromDefinition() {
        return fromDefinition;
    }

    public WhereDefinition getWhereDefinition() {
        return whereDefinition;
    }

    @Override
    public Copier copier() {
        return new Copier(this.fromDefinition, this.whereDefinition);
    }

    public class Copier implements Definition.Copier {

        private FromDefinition fromDefinition;

        private WhereDefinition whereDefinition;

        public Copier(FromDefinition fromDefinition, WhereDefinition whereDefinition) {
            this.fromDefinition = fromDefinition;
            this.whereDefinition = whereDefinition;
        }

        @Override
        public SqlDeleteDefinition copy() {
            return new SqlDeleteDefinition(wholeDesc, this.fromDefinition, this.whereDefinition);
        }
    }
}