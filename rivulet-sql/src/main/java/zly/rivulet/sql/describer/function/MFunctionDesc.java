package zly.rivulet.sql.describer.function;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.base.definition.function.MFunctionDefinition;

import java.lang.reflect.Parameter;

public interface MFunctionDesc<F, C> extends SingleValueElementDesc<F, C> {

    Class<?> getReturnType();

    MFunctionDefinition toDefinition(Parameter[] parameters);
}
