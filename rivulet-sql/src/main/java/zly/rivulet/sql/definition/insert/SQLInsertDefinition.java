package zly.rivulet.sql.definition.insert;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.query.SQLBlueprint;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.sql.parser.toolbox.SqlParserPortableToolbox;

import java.util.List;
import java.util.stream.Collectors;

public class SQLInsertDefinition extends SQLBlueprint {

    private final SQLModelMeta sqlModelMeta;

    private List<ColumnItemDefinition> columnItemDefinitionList;

    private List<List<SQLSingleValueElementDefinition>> values;

    public SQLInsertDefinition(SQLModelMeta sqlModelMeta, SqlParserPortableToolbox toolbox) {
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

    public List<List<SQLSingleValueElementDefinition>> getValues() {
        return values;
    }

    @Override
    public Assigner<?> getAssigner() {
        return null;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }
}
