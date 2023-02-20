package zly.rivulet.mysql.generator;

import zly.rivulet.sql.definition.SQLBlueprint;
import zly.rivulet.sql.generator.SQLFish;
import zly.rivulet.sql.generator.statement.SQLStatement;

public class MySQLFish extends SQLFish {
    protected MySQLFish(SQLBlueprint blueprint, SQLStatement statement) {
        super(blueprint, statement);
    }
}
