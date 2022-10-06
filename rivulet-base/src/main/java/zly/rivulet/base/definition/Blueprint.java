package zly.rivulet.base.definition;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.base.parser.ParamReceiptManager;

/**
 * Description 完全信息的Definition，设计图
 *
 * @author zhaolaiyuan
 * Date 2022/2/27 10:49
 **/
public interface Blueprint extends Definition {
    RivuletFlag getFlag();

    Assigner<?> getAssigner();

    ParamReceiptManager getParamReceiptManager();

    void putStatement(Definition key, Statement sqlStatement);

    Statement getStatement(Definition key);

    boolean isWarmUp();

    void finishWarmUp();
}
