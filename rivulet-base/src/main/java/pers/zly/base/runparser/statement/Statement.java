package pers.zly.base.runparser.statement;

public interface Statement {

    String createStatement();

    void collectStatement(StringBuilder sqlCollector);

    /**
     * Description 考虑仅开发模式支持
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 14:18
     **/
    String formatGetStatement(int tabLevel);
}
