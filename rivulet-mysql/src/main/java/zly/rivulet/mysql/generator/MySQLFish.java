package zly.rivulet.mysql.generator;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.sql.definition.query.SQLBlueprint;
import zly.rivulet.sql.generator.SQLFish;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class MySQLFish extends SQLFish {
    protected MySQLFish(SQLBlueprint blueprint, SqlStatement statement) {
        super(blueprint, statement);
    }
}
