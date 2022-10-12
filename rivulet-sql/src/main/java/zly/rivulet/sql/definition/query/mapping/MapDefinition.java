package zly.rivulet.sql.definition.query.mapping;

import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.singleValueElement.SingleValueElementDefinition;
import zly.rivulet.sql.definition.singleValueElement.SQLSingleValueElementDefinition;
import zly.rivulet.sql.parser.SQLAliasManager;

public class MapDefinition implements SQLSingleValueElementDefinition {

    private final SingleValueElementDefinition valueDefinition;

    /**
     * 引用别名，就是当前select字段的引用表别名
     **/
    private final SQLAliasManager.AliasFlag referenceAlias;

    /**
     * 别名，这个select字段自己的别名
     **/
    private final SQLAliasManager.AliasFlag aliasFlag;

    public MapDefinition(SingleValueElementDefinition valueDefinition, SQLAliasManager.AliasFlag referenceAlias, SQLAliasManager.AliasFlag aliasFlag) {
        this.valueDefinition = valueDefinition;
        this.referenceAlias = referenceAlias;
        this.aliasFlag = aliasFlag;
    }

    public SingleValueElementDefinition getValueDefinition() {
        return valueDefinition;
    }

    public SQLAliasManager.AliasFlag getAliasFlag() {
        return aliasFlag;
    }

    public SQLAliasManager.AliasFlag getReferenceAlias() {
        return referenceAlias;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }

}
