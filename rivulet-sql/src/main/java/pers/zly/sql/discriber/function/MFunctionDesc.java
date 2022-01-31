package pers.zly.sql.discriber.function;

import pers.zly.base.describer.SingleValueElementDesc;
import pers.zly.sql.definition.function.MFunctionDefinition;

import java.lang.reflect.Parameter;

public interface MFunctionDesc<F, C> extends SingleValueElementDesc<F, C> {

    Class<?> getReturnType();

    MFunctionDefinition toDefinition(Parameter[] parameters);
}
