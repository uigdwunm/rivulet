package zly.rivulet.sql.preparser;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.describer.field.FieldMapping;
import zly.rivulet.base.preparser.PreParseHelper;
import zly.rivulet.sql.SqlRivuletProperties;
import zly.rivulet.sql.describer.query.SqlQueryMetaDesc;

public class SqlPreParseHelper implements PreParseHelper {

    private final SqlPreParser sqlPreParser;

    private final SqlParamDefinitionManager sqlParamDefinitionManager;

    private final WholeDesc wholeDesc;

    private final AliasManager aliasManager;

    private final SqlRivuletProperties configProperties;

    public SqlPreParseHelper(SqlPreParser sqlPreParser, WholeDesc wholeDesc, SqlParamDefinitionManager sqlParamDefinitionManager) {
        this.sqlPreParser = sqlPreParser;
        this.sqlParamDefinitionManager = sqlParamDefinitionManager;
        this.wholeDesc = wholeDesc;
        this.aliasManager = new AliasManager();
        this.configProperties = sqlPreParser.getConfigProperties();
    }

    public void startSub() {
        aliasManager.startSub();
    }

    public void endSub() {
        aliasManager.endSub();
    }

    public SqlPreParser getSqlPreParser() {
        return sqlPreParser;
    }

    public SqlParamDefinitionManager getSqlParamDefinitionManager() {
        return sqlParamDefinitionManager;
    }

    public WholeDesc getWholeDesc() {
        return wholeDesc;
    }

    public SingleValueElementDefinition parse(SingleValueElementDesc<?, ?> singleValueElementDesc) {
        if (singleValueElementDesc instanceof FieldMapping) {
            return
        } else if (singleValueElementDesc instanceof SqlQueryMetaDesc) {
            return new
        }
        return null;
    }
}
