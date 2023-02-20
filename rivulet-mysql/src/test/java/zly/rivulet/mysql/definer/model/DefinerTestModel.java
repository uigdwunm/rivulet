package zly.rivulet.mysql.definer.model;

import zly.rivulet.sql.definer.annotations.SQLColumn;
import zly.rivulet.sql.definer.annotations.SQLTable;

@SQLTable("test")
public class DefinerTestModel {

    @SQLColumn("")
    private String strToVarchar;
}
