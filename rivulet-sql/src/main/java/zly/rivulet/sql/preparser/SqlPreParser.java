package zly.rivulet.sql.preparser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.describer.Desc;
import zly.rivulet.base.preparser.PreParser;
import zly.rivulet.base.preparser.Rock;
import zly.rivulet.sql.SqlRivuletProperties;

import java.lang.reflect.Method;

public class SqlPreParser implements PreParser {

    private final SqlRivuletProperties configProperties;

    private final ConvertorManager convertorManager;


    public SqlPreParser(SqlRivuletProperties configProperties, ConvertorManager convertorManager) {
        this.configProperties = configProperties;
        this.convertorManager = convertorManager;

        this.init();
    }

    @Override
    public Definition parse(Desc desc) {
        return null;
    }

    @Override
    public Rock bind(Definition definition, Method method) {
        SqlRock sqlRock = new SqlRock(definition, );
        SqlParamDefinitionManager sqlParamDefinitionManager = new SqlParamDefinitionManager(method.getParameters());
        return sqlRock;
    }

    private void init() {

    }
}
