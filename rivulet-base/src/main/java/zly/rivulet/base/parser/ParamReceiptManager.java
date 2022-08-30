package zly.rivulet.base.parser;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.FieldMeta;
import zly.rivulet.base.definer.outerType.SelfType;
import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.definition.param.StaticParamReceipt;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.describer.param.StandardParam;
import zly.rivulet.base.describer.param.StaticParam;
import zly.rivulet.base.exception.UnbelievableException;

import java.util.ArrayList;
import java.util.List;

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
        Convertor<?, ?> convertor = convertorManager.get(paramDesc.getParamType(), SelfType.class);
        return this.registerParam(paramDesc, convertor);
    }

    public ParamReceipt registerParam(Param<?> paramDesc, FieldMeta fieldMeta) {
        // 类型转换器
        Convertor<?, ?> convertor = convertorManager.get(paramDesc.getParamType(), fieldMeta.getOriginOuterType());
        return this.registerParam(paramDesc, convertor);
    }

    private ParamReceipt registerParam(Param<?> paramDesc, Convertor<?, ?> convertor) {
        ParamReceipt paramReceipt;
        if (paramDesc instanceof StaticParam) {
            StaticParam<?> staticParam = (StaticParam<?>) paramDesc;
            paramReceipt = new StaticParamReceipt(staticParam.getValue(), convertor);
        } else if (paramDesc instanceof StandardParam) {
            StandardParam<?> standardParam = (StandardParam<?>) paramDesc;
            paramReceipt = this.createParamDefinition(standardParam, convertor);
        } else {
            throw UnbelievableException.unknownType();
        }
        this.allParamReceiptList.add(paramReceipt);
        return paramReceipt;

    }

    protected abstract ParamReceipt createParamDefinition(StandardParam<?> paramDesc, Convertor<?, ?> convertor);

    public List<ParamReceipt> getAllParamReceiptList() {
        return allParamReceiptList;
    }
}