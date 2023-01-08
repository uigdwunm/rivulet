package zly.rivulet.mysql.util;

import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.base.utils.collector.CommonStatementCollector;

public class MySQLPrintUtils {

    public static String commonPrint(Fish fish) {
        Statement statement = fish.getStatement();
        CommonStatementCollector commonStatementCollector = new CommonStatementCollector();

        statement.collectStatementOrCache(commonStatementCollector);
        return commonStatementCollector.toString();
    }

    public static String formatPrint(Fish fish) {
        Statement statement = fish.getStatement();
        MySQLFormatStatementCollector statementCollector = new MySQLFormatStatementCollector();

        statement.collectStatementOrCache(statementCollector);
        return statementCollector.toString();
    }

}
