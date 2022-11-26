package zly.rivulet.sql.definition.update;

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
import zly.rivulet.sql.describer.param.SqlParamCheckType;
import zly.rivulet.sql.describer.update.SqlUpdateMetaDesc;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.SqlParser;
import zly.rivulet.sql.parser.proxy_node.FromNode;
import zly.rivulet.sql.parser.proxy_node.ProxyNodeManager;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

public class SqlUpdateDefinition extends SQLBlueprint {

    private final SqlUpdateMetaDesc<?> metaDesc;

    private final FromDefinition fromDefinition;

    private final SetDefinition setDefinition;

    private final WhereDefinition whereDefinition;

    private final SQLUpdateResultAssigner assigner = new SQLUpdateResultAssigner();

    private SqlUpdateDefinition(SqlUpdateMetaDesc<?> metaDesc, FromDefinition fromDefinition, SetDefinition setDefinition, WhereDefinition whereDefinition) {
        super(RivuletFlag.UPDATE, metaDesc);
        this.metaDesc = metaDesc;
        this.fromDefinition = fromDefinition;
        this.setDefinition = setDefinition;
        this.whereDefinition = whereDefinition;
    }

    public SqlUpdateDefinition(SqlParserPortableToolbox toolbox, WholeDesc wholeDesc) {
        super(RivuletFlag.UPDATE, wholeDesc);
        this.metaDesc = (SqlUpdateMetaDesc<?>) wholeDesc;
        this.aliasManager = new SQLAliasManager(toolbox.getConfigProperties());
        Class<?> mainFrom = metaDesc.getMainFrom();
        if (ClassUtils.isExtend(QueryComplexModel.class, mainFrom)) {
            // 仅支持单表更新
            throw SQLDescDefineException.unSupportMultiModelUpdate();
        }
        // 获取QueryProxyNode
        ProxyNodeManager proxyModelManager = toolbox.getSqlPreParser().getProxyModelManager();
        QueryProxyNode queryProxyNode = proxyModelManager.getOrCreateQueryProxyNode(toolbox, metaDesc.getAnnotation(), metaDesc.getMainFrom());
        toolbox.setQueryProxyNode(queryProxyNode);

        this.fromDefinition = new FromDefinition(toolbox);

        this.setDefinition = new SetDefinition(toolbox, metaDesc.getMappedItemList());

        ConditionContainer<?, ?> whereConditionContainer = metaDesc.getWhereConditionContainer();
        if (whereConditionContainer != null) {
            this.whereDefinition = new WhereDefinition(toolbox, whereConditionContainer);
        } else {
            this.whereDefinition = null;
        }

        this.paramReceiptManager = toolbox.getParamReceiptManager();
        this.aliasManager.init(queryProxyNode);
    }

    public SqlUpdateDefinition(SqlParserPortableToolbox toolbox, SQLModelMeta sqlModelMeta, SQLFieldMeta primaryKey) {
        super(RivuletFlag.UPDATE, null);
        this.metaDesc = null;
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

        this.setDefinition = new SetDefinition(toolbox, sqlModelMeta);

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
        return RivuletFlag.UPDATE;
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

    public SetDefinition getSetDefinition() {
        return setDefinition;
    }

    public WhereDefinition getWhereDefinition() {
        return whereDefinition;
    }

    @Override
    public Copier copier() {
        return null;
    }

    public class Copier implements Definition.Copier {

        private SqlUpdateMetaDesc<?> metaDesc;

        private FromDefinition fromDefinition;

        private SetDefinition setDefinition;

        private WhereDefinition whereDefinition;

        private Copier(SqlUpdateMetaDesc<?> metaDesc, FromDefinition fromDefinition, SetDefinition setDefinition, WhereDefinition whereDefinition) {
            this.metaDesc = metaDesc;
            this.fromDefinition = fromDefinition;
            this.setDefinition = setDefinition;
            this.whereDefinition = whereDefinition;
        }

        public void setMetaDesc(SqlUpdateMetaDesc<?> metaDesc) {
            this.metaDesc = metaDesc;
        }

        public void setFromDefinition(FromDefinition fromDefinition) {
            this.fromDefinition = fromDefinition;
        }

        public void setSetDefinition(SetDefinition setDefinition) {
            this.setDefinition = setDefinition;
        }

        public void setWhereDefinition(WhereDefinition whereDefinition) {
            this.whereDefinition = whereDefinition;
        }

        @Override
        public SqlUpdateDefinition copy() {
            return new SqlUpdateDefinition(metaDesc, fromDefinition, setDefinition, whereDefinition);
        }
    }
}