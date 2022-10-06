package zly.rivulet.sql.generator.toolbox;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_model_meta.ModelBatchParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.CommonParamManager;
import zly.rivulet.base.utils.PortableToolbox;
import zly.rivulet.sql.definition.query.SQLBlueprint;
import zly.rivulet.sql.generator.statement.SqlStatement;
import zly.rivulet.sql.parser.SQLAliasManager;

import java.util.HashMap;
import java.util.Map;

public class GenerateToolbox implements PortableToolbox {
    // sql长度统计
    private int length;

    private final ParamManager paramManager;

    private final SQLAliasManager aliasManager;

    /**
     * definition类和statement之间的缓存映射
     **/
    private final Map<Definition, SqlStatement> statementCache;

    private final SQLBlueprint blueprint;

    public GenerateToolbox(ParamManager paramManager, SQLBlueprint blueprint) {
        this.paramManager = paramManager;
        this.aliasManager = blueprint.getAliasManager();
        statementCache = blueprint.getStateMentCache();
        this.blueprint = blueprint;
    }

    public int getLength() {
        return length;
    }

    public void addLength(int length) {
        this.length += length;
    }

    public CommonParamManager getParamManager() {
        return (CommonParamManager) paramManager;
    }

    public ModelBatchParamManager getBatchParamManager() {
        if (paramManager instanceof ModelBatchParamManager) {
            return (ModelBatchParamManager) paramManager;
        }
        return null;
    }

    public SQLAliasManager getAliasManager() {
        return aliasManager;
    }

    public SQLBlueprint getBlueprint() {
        return blueprint;
    }
}
