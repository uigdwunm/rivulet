package zly.rivulet.base.parser;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.StandardParam;
import zly.rivulet.base.describer.param.StaticParam;
import zly.rivulet.base.exception.ParamDefineException;
import zly.rivulet.base.exception.UnbelievableException;
import zly.rivulet.base.generator.param_manager.LazyParamManager;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.SimpleParamManager;
import zly.rivulet.base.utils.ArrayUtils;
import zly.rivulet.base.utils.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Description 对应每个查询方法都有一个
 *
 * @author zhaolaiyuan
 * Date 2022/2/26 9:25
 **/
public abstract class ParamReceiptManager {

    public final ConvertorManager convertorManager;

    public final List<ParamReceipt> allParamReceiptList = new ArrayList<>();

    protected ParamReceiptManager(ConvertorManager convertorManager) {
        this.convertorManager = convertorManager;
    }

    public ParamReceipt registerParam(Param<?> paramDesc) {
        return this.registerParam(paramDesc, null);
    }

    public ParamReceipt registerParam(Param<?> paramDesc, FieldMeta fieldMeta) {
        ParamReceipt paramReceipt = this.createParamDefinition(paramDesc, fieldMeta);

        this.allParamReceiptList.add(paramReceipt);
        return paramReceipt;
    }

    protected abstract ParamReceipt createParamDefinition(Param<?> paramDesc, FieldMeta fieldMeta);

    public List<ParamReceipt> getAllParamReceiptList() {
        return allParamReceiptList;
    }
}
