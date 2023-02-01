package zly.rivulet.sql;

import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.pipeline.ExecutePlan;
import zly.rivulet.base.utils.TwofoldConcurrentHashMap;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.sql.convertor.*;

public abstract class SQLRivuletManager extends RivuletManager {

    protected final TwofoldConcurrentHashMap<RivuletFlag, Class<?>, ExecutePlan> rivuletFlagClassExecutePlanMap;

    protected SQLRivuletManager(Generator generator, WarehouseManager warehouseManager) {
        super(generator.getParser(), generator, generator.getProperties(), generator.getConvertorManager(), warehouseManager);
        rivuletFlagClassExecutePlanMap = new TwofoldConcurrentHashMap<>();

    }

    public void registerExecutePlan(RivuletFlag rivuletFlag, Class<?> returnType, ExecutePlan executePlan) {
        this.rivuletFlagClassExecutePlanMap.put(rivuletFlag, returnType, executePlan);
    }

    public static ConvertorManager cretateDefaultConvertorManager() {
        ConvertorManager convertorManager = new ConvertorManager();
        // 注册默认的语句转换器(用于将入参转换成语句)
        DefaultStatementConvertors.register(convertorManager);

        // 注册结果转换器(用于将查询结果转换成语句)
        DefaultResultStringConvertors.register(convertorManager);

        DefaultResultBooleanConvertors.register(convertorManager);

        DefaultResultIntegerConvertors.register(convertorManager);
        DefaultResultLongConvertors.register(convertorManager);
        DefaultResultBigIntegerConvertors.register(convertorManager);

        DefaultResultFloatConvertors.register(convertorManager);
        DefaultResultDoubleConvertors.register(convertorManager);
        DefaultResultBigDecimalConvertors.register(convertorManager);

        DefaultResultSQLDateConvertors.register(convertorManager);
        DefaultResultSQLTimeConvertors.register(convertorManager);
        DefaultResultSQLTimestampConvertors.register(convertorManager);
        DefaultResultLocalDateTimeConvertors.register(convertorManager);

        return convertorManager;
    }

}
