package zly.rivulet.sql.definition.query;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.sql.preparser.SQLAliasManager;

public interface SQLBlueprint extends Blueprint {

    SQLAliasManager getAliasManager();

}
