package zly.rivulet.sql.definition.query;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.definition.Definition;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.sql.parser.SQLAliasManager;

/**
 * Description 标识一个未完成的Definition
 * 用于解决循环嵌套子查询
 *
 * @author zhaolaiyuan
 * Date 2022/5/21 10:56
 **/
public class HalfBlueprint implements SQLBlueprint {

    public static final HalfBlueprint instance = new HalfBlueprint();

    public static boolean isHalf(Blueprint blueprint) {
        return blueprint instanceof HalfBlueprint;
    }

    @Override
    public Definition forAnalyze() {
        return null;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public RivuletFlag getFlag() {
        return null;
    }

    @Override
    public Assigner<?> getAssigner() {
        return null;
    }

    @Override
    public ParamReceiptManager getParamReceiptManager() {
        return null;
    }

    private HalfBlueprint() {}

    @Override
    public SQLAliasManager getAliasManager() {
        return null;
    }
}
