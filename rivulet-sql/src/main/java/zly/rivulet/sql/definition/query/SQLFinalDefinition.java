package zly.rivulet.sql.definition.query;

import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.sql.preparser.SQLAliasManager;

public interface SQLFinalDefinition extends FinalDefinition {

    SQLAliasManager getAliasManager();

}
