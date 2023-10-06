package zly.rivulet.sql.definition.insert;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.SQLBlueprint;
import zly.rivulet.sql.parser.toolbox_.SQLParserPortableToolbox;

import java.util.List;
import java.util.stream.Collectors;

public class SQLInsertDefinition extends SQLBlueprint {

    private final SQLModelMeta sqlModelMeta;

    private final List<ColumnItemDefinition> columnItemDefinitionList;

//    private final List<List<SQLSingleValueElementDefinition>> values;

    public SQLInsertDefinition(SQLModelMeta sqlModelMeta, List<ColumnItemDefinition> columnItemDefinitionList) {
        super(RivuletFlag.INSERT, null);
        this.sqlModelMeta = sqlModelMeta;
        this.columnItemDefinitionList = columnItemDefinitionList;
    }

    public SQLInsertDefinition(SQLModelMeta sqlModelMeta, SQLParserPortableToolbox toolbox) {
        super(RivuletFlag.INSERT, null);
        this.sqlModelMeta = sqlModelMeta;
        this.columnItemDefinitionList = sqlModelMeta.getFieldMetaList().stream()
            .map(fieldMeta -> new ColumnItemDefinition(toolbox, (SQLFieldMeta) fieldMeta))
            .collect(Collectors.toList());
    }

    public SQLModelMeta getSqlModelMeta() {
        return sqlModelMeta;
    }

    public List<ColumnItemDefinition> getColumnItemDefinitionList() {
        return columnItemDefinitionList;
    }

    @Override
    public Assigner<?> getAssigner() {
        return null;
    }

    @Override
    public Copier copier() {
        return new Copier(sqlModelMeta, columnItemDefinitionList);
    }

    public class Copier implements Definition.Copier {

        private final SQLModelMeta sqlModelMeta;

        private final List<ColumnItemDefinition> columnItemDefinitionList;

        public Copier(SQLModelMeta sqlModelMeta, List<ColumnItemDefinition> columnItemDefinitionList) {
            this.sqlModelMeta = sqlModelMeta;
            this.columnItemDefinitionList = columnItemDefinitionList;
        }

        @Override
        public SQLInsertDefinition copy() {
            return new SQLInsertDefinition(sqlModelMeta, columnItemDefinitionList);
        }
    }
}
