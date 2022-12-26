package zly.rivulet.sql;

import zly.rivulet.base.DefaultOperation;
import zly.rivulet.base.RivuletManager;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definer.enums.RivuletFlag;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.describer.WholeDesc;
import zly.rivulet.base.exception.ExecuteException;
import zly.rivulet.base.exception.ParseException;
import zly.rivulet.base.generator.Generator;
import zly.rivulet.base.generator.param_manager.ParamManager;
import zly.rivulet.base.generator.param_manager.for_proxy_method.SimpleParamManager;
import zly.rivulet.base.pipeline.ExecutePlan;
import zly.rivulet.base.utils.ClassUtils;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.base.utils.TwofoldConcurrentHashMap;
import zly.rivulet.base.warehouse.WarehouseManager;
import zly.rivulet.sql.convertor.SQLDefaultConvertor;
import zly.rivulet.sql.definition.SQLBlueprint;
import zly.rivulet.sql.definition.query.SqlQueryDefinition;
import zly.rivulet.sql.pipeline.SQLQueryManyExecutePlan;
import zly.rivulet.sql.pipeline.SQLQueryOneExecutePlan;
import zly.rivulet.sql.pipeline.SQLUpdateOneExecutePlan;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public abstract class SQLRivuletManager extends RivuletManager {

    protected final TwofoldConcurrentHashMap<RivuletFlag, Class<?>, ExecutePlan> rivuletFlagClassExecutePlanMap;

    protected SQLRivuletManager(Generator generator, WarehouseManager warehouseManager) {
        super(generator.getParser(), generator, generator.getProperties(), generator.getConvertorManager(), warehouseManager);
        rivuletFlagClassExecutePlanMap = new TwofoldConcurrentHashMap<>();

        // 注册默认的转换器
        SQLDefaultConvertor.registerDefault(generator.getConvertorManager());
    }

    public void registerExecutePlan(RivuletFlag rivuletFlag, Class<?> returnType, ExecutePlan executePlan) {
        this.rivuletFlagClassExecutePlanMap.put(rivuletFlag, returnType, executePlan);
    }

}
