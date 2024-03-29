package zly.rivulet.sql.definition.delete;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.ParamCheckType;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.sql.assigner.SQLUpdateResultAssigner;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.SQLBlueprint;
import zly.rivulet.sql.definition.query.main.FromDefinition;
import zly.rivulet.sql.definition.query.main.WhereDefinition;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.definition.query.operate.AndOperateDefinition;
import zly.rivulet.sql.definition.query.operate.EqOperateDefinition;
import zly.rivulet.sql.describer.condition.common.ConditionContainer;
import zly.rivulet.sql.describer.delete.SQLDeleteMetaDesc;
import zly.rivulet.sql.describer.join.QueryComplexModel;
import zly.rivulet.sql.exception.SQLDescDefineException;
import zly.rivulet.sql.parser.SQLAliasManager;
import zly.rivulet.sql.parser.SQLParser;
import zly.rivulet.sql.parser.proxy_node.FromNode;
import zly.rivulet.sql.parser.proxy_node.ProxyNodeManager;
import zly.rivulet.sql.parser.proxy_node.QueryProxyNode;
import zly.rivulet.sql.parser.toolbox.SQLParserPortableToolbox;

public class SQLDeleteDefinition extends SQLBlueprint {

    private final FromDefinition fromDefinition;

    private final WhereDefinition whereDefinition;

    private final SQLUpdateResultAssigner assigner = new SQLUpdateResultAssigner();

    private SQLDeleteDefinition(WholeDesc wholeDesc, FromDefinition fromDefinition, WhereDefinition whereDefinition) {
        super(RivuletFlag.DELETE, wholeDesc);
        this.fromDefinition = fromDefinition;
        this.whereDefinition = whereDefinition;
    }

    public SQLDeleteDefinition(SQLParserPortableToolbox toolbox, WholeDesc wholeDesc) {
        super(RivuletFlag.DELETE, wholeDesc);
        SQLDeleteMetaDesc<?> metaDesc = (SQLDeleteMetaDesc<?>) wholeDesc;
        this.aliasManager = new SQLAliasManager(toolbox.getConfigProperties());
        Class<?> mainFrom = metaDesc.getMainFrom();
        if (ClassUtils.isExtend(QueryComplexModel.class, mainFrom)) {
            // 仅支持单表删除
            throw SQLDescDefineException.unSupportMultiModelUpdate();
        }
        // 获取QueryProxyNode
        ProxyNodeManager proxyModelManager = toolbox.getSqlPreParser().getProxyModelManager();
        QueryProxyNode queryProxyNode = proxyModelManager.createQueryProxyNode(toolbox, metaDesc.getAnnotation(), mainFrom);
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

    public SQLDeleteDefinition(SQLParserPortableToolbox toolbox, SQLModelMeta sqlModelMeta, SQLFieldMeta primaryKey) {
        super(RivuletFlag.DELETE, null);
        Class<?> mainFrom = sqlModelMeta.getModelClass();
        if (ClassUtils.isExtend(QueryComplexModel.class, mainFrom)) {
            // 仅支持单表更新
            throw SQLDescDefineException.unSupportMultiModelUpdate();
        }
        SQLParser sqlParser = toolbox.getSqlPreParser();
        ProxyNodeManager proxyModelManager = sqlParser.getProxyModelManager();
        QueryProxyNode queryProxyNode = proxyModelManager.createQueryProxyNode(toolbox, sqlModelMeta);
        toolbox.setQueryProxyNode(queryProxyNode);

        this.fromDefinition = new FromDefinition(toolbox);

        Param<?> mainIdParam = Param.of(primaryKey.getFieldType(), Constant.MAIN_ID, ParamCheckType.NATURE);
        FromNode fromNode = queryProxyNode.getFromNodeList().get(0);
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
        public SQLDeleteDefinition copy() {
            return new SQLDeleteDefinition(wholeDesc, this.fromDefinition, this.whereDefinition);
        }
    }
}