package zly.rivulet.mysql.generator.statement.update;

import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.View;
import zly.rivulet.base.utils.collector.StatementCollector;
import zly.rivulet.sql.definition.update.SetDefinition;
import zly.rivulet.sql.generator.SqlStatementFactory;
import zly.rivulet.sql.generator.statement.SqlStatement;

import java.util.List;
import java.util.stream.Collectors;

public class SetStatement extends SqlStatement {

    private final SetDefinition setDefinition;

    private final View<SetItemStatement> setItemStatementView;

    private static final String SET = "SET ";

    public SetStatement(SetDefinition setDefinition, View<SetItemStatement> setItemStatementView) {
        this.setDefinition = setDefinition;
        this.setItemStatementView = setItemStatementView;
    }

    @Override
    protected int length() {
        return SET.length() + (setItemStatementView.size() - 1) + setItemStatementView.stream().map(SetItemStatement::getLengthOrCache).reduce(0, Integer::sum);
    }

    @Override
    public void collectStatement(StatementCollector collector) {
        collector.append(SET);
        for (SetItemStatement item : collector.createJoiner(Constant.COMMA, setItemStatementView)) {
            item.collectStatement(collector);
        }
    }

    public static void registerToFactory(SqlStatementFactory sqlStatementFactory) {
        sqlStatementFactory.register(
            SetDefinition.class,
            (definition, soleFlag, toolbox) -> {
                SetDefinition setDefinition = (SetDefinition) definition;
                List<SqlStatement> setItemDefinitionList = setDefinition.getSetItemDefinitionView().stream()
                    .map(itemDefinition -> sqlStatementFactory.warmUp(itemDefinition, soleFlag.subSwitch(), toolbox))
                    .collect(Collectors.toList());

                return new SetStatement(setDefinition, (View) View.create(setItemDefinitionList));
            },
            (definition, toolbox) -> {
                SetDefinition setDefinition = (SetDefinition) definition;
                List<SqlStatement> setItemDefinitionList = setDefinition.getSetItemDefinitionView().stream()
                    .map(itemDefinition -> sqlStatementFactory.getOrCreate(itemDefinition, toolbox))
                    .collect(Collectors.toList());

                return new SetStatement(setDefinition, (View) View.create(setItemDefinitionList));
            }
        );
    }
}
