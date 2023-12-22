package zly.rivulet.sql.definition.query.main;

import zly.rivulet.base.definition.AbstractDefinition;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.checkCondition.CheckCondition;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definer.meta.SQLQueryMeta;
import zly.rivulet.sql.describer.select.item.JoinItem;
import zly.rivulet.sql.parser.toolbox.SQLParserPortableToolbox;

import java.util.List;
import java.util.stream.Collectors;

public class FromDefinition extends AbstractDefinition {
    private final QueryFromMeta mainFrom;

    private final List<JoinRelationDefinition> joinRelations;

    private FromDefinition(CheckCondition checkCondition, QueryFromMeta mainFrom, List<JoinRelationDefinition> joinRelations) {
        super(checkCondition, null);
        this.mainFrom = mainFrom;
        this.joinRelations = joinRelations;
    }

    public FromDefinition(
            SQLParserPortableToolbox toolbox,
            SQLQueryMeta from,
            List<JoinItem> joinList
    ) {
        super(CheckCondition.IS_TRUE, toolbox.getParamReceiptManager());

        this.mainFrom = toolbox.parseQueryFromMeta(from);
        this.joinRelations = joinList.stream()
                .map(joinItem -> new JoinRelationDefinition(toolbox, joinItem))
                .collect(Collectors.toList());

    }


    public Class<?> getFromMode() {
        if (mainFrom instanceof SQLModelMeta) {
            return ((SQLModelMeta) mainFrom).getModelClass();
        } else {
            // 随便返回一个,无意义
            return null;
        }
    }

    public QueryFromMeta getMainFrom() {
        return mainFrom;
    }

    @Override
    public Copier copier() {
        return new Copier(this.mainFrom, this.joinRelations);
    }

    public class Copier implements Definition.Copier {
        private QueryFromMeta mainFrom;

        private List<JoinRelationDefinition> joinRelations;

        public Copier(QueryFromMeta mainFrom, List<JoinRelationDefinition> joinRelations) {
            this.mainFrom = mainFrom;
            this.joinRelations = joinRelations;
        }

        public void setMainFrom(QueryFromMeta mainFrom) {
            this.mainFrom = mainFrom;
        }

        public void setJoinRelations(List<JoinRelationDefinition> joinRelations) {
            this.joinRelations = joinRelations;
        }

        @Override
        public FromDefinition copy() {
            return new FromDefinition(checkCondition, mainFrom, joinRelations);
        }
    }
}
