package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.definition.function.MFunctionDefinitionSQL;

import java.lang.reflect.Parameter;

public interface MFunctionDesc<F, C> extends SingleValueElementDesc<F, C> {

    Class<?> getReturnType();

    MFunctionDefinitionSQL toDefinition(Parameter[] parameters);
}
