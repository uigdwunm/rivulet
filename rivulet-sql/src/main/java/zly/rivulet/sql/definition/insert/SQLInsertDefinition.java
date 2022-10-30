package zly.rivulet.sql.definition.insert;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.sql.definer.meta.SQLModelMeta;
import zly.rivulet.sql.definition.query.SQLBlueprint;

public class SQLInsertDefinition extends SQLBlueprint {

    private final SQLModelMeta sqlModelMeta;

    public SQLInsertDefinition(SQLModelMeta sqlModelMeta) {
        super(RivuletFlag.INSERT, null);
        this.sqlModelMeta = sqlModelMeta;
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
