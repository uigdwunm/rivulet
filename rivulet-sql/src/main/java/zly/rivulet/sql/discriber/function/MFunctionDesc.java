package zly.rivulet.sql.discriber.function;

import zly.rivulet.base.describer.SingleValueElementDesc;
import zly.rivulet.sql.definition.function.MFunctionDefinition;

import java.lang.reflect.Parameter;

public interface MFunctionDesc<F, C> extends SingleValueElementDesc<F, C> {

    Class<?> getReturnType();

    MFunctionDefinition toDefinition(Parameter[] parameters);
}
