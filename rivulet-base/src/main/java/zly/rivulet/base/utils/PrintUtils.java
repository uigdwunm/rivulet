package zly.rivulet.base.utils;

import zly.rivulet.base.generator.Fish;
import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.base.utils.collector.CommonStatementCollector;

public class PrintUtils {

    public static String commonPrint(Fish fish) {
        Statement statement = fish.getStatement();
        CommonStatementCollector commonStatementCollector = new CommonStatementCollector();

        statement.collectStatementOrCache(commonStatementCollector);
        return commonStatementCollector.toString();
    }

}
