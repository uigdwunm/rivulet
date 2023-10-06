package zly.rivulet.sql.describer.meta;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.sql.definer.meta.QueryFromMeta;
import zly.rivulet.sql.parser.SQLAliasManager;

public abstract class SQLTableMeta implements SQLQueryMeta, QueryFromMeta {

    private SQLAliasManager.AliasFlag aliasFlag;
    public abstract void primaryKey();

    public abstract String getTableName();

    @Override
    public SQLAliasManager.AliasFlag getAliasFlag() {
        if (aliasFlag == null) {
            this.aliasFlag = SQLAliasManager.createAlias(this.getTableName());
        }
        return aliasFlag;
    }

    @Override
    public Copier copier() {
        SQLTableMeta oneself = this;
        return new Copier() {
            @Override
            public Definition copy() {
                return oneself;
            }
        };
    }
}
