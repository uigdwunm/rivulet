package zly.rivulet.sql.definition.query.mapping;

import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.describer.field.SelectMapping;
import zly.rivulet.sql.preparser.SQLAliasManager;
import zly.rivulet.sql.preparser.helper.SqlPreParseHelper;

public class MappingDefinition {

    private SingleValueElementDefinition definition;

    private final SelectMapping<?, ?> itemMapper;

    private SQLAliasManager.AliasFlag aliasFlag;

    public MappingDefinition(SqlPreParseHelper sqlPreParseHelper, SingleValueElementDesc<?, ?> desc, SelectMapping<?, ?> itemMapper) {
        this.definition = sqlPreParseHelper.parse(desc);
        this.itemMapper = itemMapper;
        SQLAliasManager aliasManager = sqlPreParseHelper.getAliasManager();
        this.aliasFlag = aliasManager.createFlag();
    }

}
