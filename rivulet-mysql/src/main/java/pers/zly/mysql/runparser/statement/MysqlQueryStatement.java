package pers.zly.mysql.runparser.statement;

import pers.zly.base.definition.AbstractDefinition;
import pers.zly.base.definition.Definition;
import pers.zly.base.runparser.ParamManager;
import pers.zly.base.runparser.StatementFactory;
import pers.zly.base.runparser.statement.AbstractStatement;
import pers.zly.base.utils.RelationSwitch;
import pers.zly.base.utils.StringUtil;
import pers.zly.sql.definition.query.SqlQueryDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class MysqlQueryStatement extends AbstractStatement {

    private final SqlQueryDefinition definition;

    /**
     * 子语句列表，select、from、where之类的
     **/
    private final List<AbstractStatement> subStatementList;

    MysqlQueryStatement(
        Definition definition,
        RelationSwitch cacheSwitch,
        StatementFactory statementFactory
    ) {
        super(definition, cacheSwitch, statementFactory);

        SqlQueryDefinition sqlQueryDefinition = (SqlQueryDefinition) definition;
        this.definition = sqlQueryDefinition;

        List<AbstractStatement> subStatementList = new ArrayList<>(sqlQueryDefinition.getSubDefinitionList().size());

        for (AbstractDefinition subDefinition : sqlQueryDefinition.getSubDefinitionList()) {
            RelationSwitch subSwitch = cacheSwitch.subSwitch();
            if (subDefinition.isNeedCheck()) {
                // 有检测条件，父级缓存失效掉
                subSwitch.parentInvalid();
            }
            AbstractStatement subStatement = statementFactory.init(subDefinition, subSwitch);
            subStatementList.add(subStatement);
        }
        this.subStatementList = subStatementList;
    }

    @Override
    public SqlQueryDefinition getOriginDefinition() {
        return this.definition;
    }

    @Override
    public String createStatement() {

        StringJoiner stringJoiner = new StringJoiner(" ");
        for (AbstractStatement statement : subStatementList) {
            stringJoiner.add(statement.createStatement());
        }

        return stringJoiner.toString();
    }

    @Override
    public void collectStatement(StringBuilder sqlCollector) {
        if (super.canCacheStr() && super.cache != null) {
            sqlCollector.append(' ').append(super.cache);
            return;
        }

        for (AbstractStatement statement : subStatementList) {
            statement.collectStatement(sqlCollector);
        }
    }

    @Override
    public String formatGetStatement(int tabLevel) {
        String tab = StringUtil.multiStr("  ", tabLevel);
        StringJoiner stringJoiner = new StringJoiner(System.lineSeparator() + tab);
        for (AbstractStatement statement : subStatementList) {
            stringJoiner.add(statement.formatGetStatement(tabLevel + 1));
        }
        return stringJoiner.toString();
    }
}
