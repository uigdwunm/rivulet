package zly.rivulet.sql.parser.analyzer;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.parser.Analyzer;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.definition.query.main.FromDefinition;
import zly.rivulet.sql.definition.query.main.SelectDefinition;
import zly.rivulet.sql.definition.query.mapping.MapDefinition;
import zly.rivulet.sql.parser.SQLAliasManager;

public class DefaultSQLAnalyzer implements Analyzer {
    @Override
    public Blueprint analyze(Blueprint blueprint) {
        if (blueprint instanceof SqlQueryDefinition) {
            SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) blueprint;
            FromDefinition fromDefinition = sqlQueryDefinition.getFromDefinition();
            SqlQueryDefinition.Copier sqlQueryDefinitionCopier = sqlQueryDefinition.copier();
            SQLAliasManager.Copier aliasManagerCopier = sqlQueryDefinition.getAliasManager().copier();
            SelectDefinition selectDefinition = sqlQueryDefinition.getSelectDefinition();
            // 最外层别名可以不要
            for (MapDefinition mapDefinition : selectDefinition.getMapDefinitionList()) {
                aliasManagerCopier.removeAlias(mapDefinition.getAliasFlag());
            }


            if (fromDefinition.getJoinRelations().isEmpty() && fromDefinition.getMainFrom() instanceof SQLModelMeta) {
                // 单表查询，可以不要别名
                aliasManagerCopier.removeAlias(fromDefinition.getMainFromAliasFlag());
            }
            sqlQueryDefinitionCopier.setAliasManagerCopier(aliasManagerCopier);
            blueprint = sqlQueryDefinitionCopier.copy();
        }
        return blueprint;
    }
}
