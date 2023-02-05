package zly.rivulet.mysql.definer.model;

import zly.rivulet.sql.definer.annotations.SqlColumn;
import zly.rivulet.sql.definer.annotations.SQLTable;

@SQLTable("test")
public class DefinerTestModel {

    @SqlColumn("")
    private String strToVarchar;
}
