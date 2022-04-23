package zly.rivulet.base.definition;

import zly.rivulet.base.preparser.PreParseHelper;

@FunctionalInterface
public interface FinalDefinitionCreator<P extends PreParseHelper, R extends FinalDefinition> {

    R create(P preParser);
}
