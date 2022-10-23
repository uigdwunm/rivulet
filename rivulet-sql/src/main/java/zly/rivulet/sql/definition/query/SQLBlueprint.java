package zly.rivulet.sql.definition.query;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.sql.parser.SQLAliasManager;

import java.util.Map;

public interface SQLBlueprint extends Blueprint {

    SQLAliasManager getAliasManager();

    Map<Class<? extends Definition>, ParamReceipt> getCustomStatementMap();

}
