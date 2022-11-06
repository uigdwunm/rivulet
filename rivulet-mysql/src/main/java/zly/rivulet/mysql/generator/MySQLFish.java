package zly.rivulet.mysql.generator;

import zly.rivulet.sql.definition.SQLBlueprint;
import zly.rivulet.sql.generator.SQLFish;
import zly.rivulet.sql.generator.statement.SqlStatement;

public class MySQLFish extends SQLFish {
    protected MySQLFish(SQLBlueprint blueprint, SqlStatement statement) {
        super(blueprint, statement);
    }
}
