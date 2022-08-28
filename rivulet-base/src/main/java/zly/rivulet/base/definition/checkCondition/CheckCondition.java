package zly.rivulet.base.definition.checkCondition;


import zly.rivulet.base.definition.param.ParamReceipt;
import zly.rivulet.base.describer.param.Param;
import zly.rivulet.base.parser.ParamReceiptManager;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.utils.StringUtil;

import java.lang.reflect.Array;
import java.util.Collection;

public abstract class CheckCondition {

    private CheckCondition(Param<?> ... params) {
        this.params = params;
    }

    protected Param<?>[] params;

    // 解析后的参数
    private ParamReceipt[] paramReceipts;

    public boolean checkCondition(ParamManager paramManager) {
        if (paramReceipts == null) {
            return check();
        }
        int length = this.paramReceipts.length;
        Object[] paramValues = new Object[length];
        for (int i = 0; i < length; i++) {
            paramValues[i] = paramManager.getParam(paramReceipts[i]);
        }

        return check(paramValues);
    }

    protected abstract boolean check(Object ... paramValues);

    public void registerParam(ParamReceiptManager paramReceiptManager) {
        if (params == null) {
            return;
        }
        int length = params.length;
        this.paramReceipts = new ParamReceipt[length];
        for (int i = 0; i < length; i++) {
            paramReceipts[i] = paramReceiptManager.registerParam(params[i]);
        }
    }

    /**
     * Description 常量，给一个永远返回正确的，给不需要检查条件的
     *
     * @author zhaolaiyuan
     * Date 2022/4/17 11:15
     **/
    public static final CheckCondition IS_TRUE = new CheckCondition() {
        @Override
        public boolean check(Object... paramValues) {
            return true;
        }
    };

    public static CheckCondition notNull(Param<?> param) {
        return new CheckCondition(param) {
            @Override
            protected boolean check(Object... paramValues) {
                return paramValues[0] != null;
            }
        };
    }

    public static CheckCondition notBlank(Param<String> param) {
        return new CheckCondition(param) {
            @Override
            protected boolean check(Object... paramValues) {
                return StringUtil.isNotBlank((String) paramValues[0]);
            }
        };
    }


    public static CheckCondition notEmpty(Param<?> param) {
        return new CheckCondition(param) {
            @Override
            protected boolean check(Object... paramValues) {
                Object paramValue = paramValues[0];
                if (paramValue == null) {
                    return false;
                }
                if (paramValue instanceof Collection) {
                    return !((Collection<?>) paramValue).isEmpty();
                }
                if (paramValue.getClass().isArray()) {
                    return Array.getLength(paramValue) > 0;
                }
                throw new IllegalArgumentException();
            }
        };
    }
}
