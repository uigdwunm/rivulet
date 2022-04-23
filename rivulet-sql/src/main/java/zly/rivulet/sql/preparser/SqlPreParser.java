package zly.rivulet.sql.preparser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.Definer;
import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.preparser.PreParser;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SqlPreParser implements PreParser {

    private final SqlRivuletProperties configProperties;

    private final ConvertorManager convertorManager;

    private final Definer definer;

    private final Map<Class<? extends WholeDesc>, BiFunction<SqlPreParseHelper, WholeDesc, FinalDefinition>> finalDefinitionCreatorMap;

    public SqlPreParser(Definer definer, SqlRivuletProperties configProperties, ConvertorManager convertorManager) {
        this.configProperties = configProperties;
        this.convertorManager = convertorManager;
        this.definer = definer;
        this.finalDefinitionCreatorMap = new HashMap<>();
        this.initFinalDefinitionCreator();
    }

    private void initFinalDefinitionCreator() {
        this.finalDefinitionCreatorMap.put(SqlQueryMetaDesc.class, SqlQueryDefinition::new);
    }

    @Override
    public FinalDefinition parse(WholeDesc wholeDesc, Method method) {
        SqlParamDefinitionManager sqlParamDefinitionManager = new SqlParamDefinitionManager(method.getParameters(), convertorManager);
        SqlPreParseHelper sqlPreParseHelper = new SqlPreParseHelper(this, wholeDesc, sqlParamDefinitionManager);
        BiFunction<SqlPreParseHelper, WholeDesc, FinalDefinition> creator = finalDefinitionCreatorMap.get(wholeDesc.getClass());
        return creator.apply(sqlPreParseHelper, wholeDesc);
    }

    public SqlRivuletProperties getConfigProperties() {
        return configProperties;
    }

    public ConvertorManager getConvertorManager() {
        return convertorManager;
    }

    public Definer getDefiner() {
        return definer;
    }
}
