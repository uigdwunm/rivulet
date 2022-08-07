package zly.rivulet.base.definition;

import zly.rivulet.base.preparser.helper.PreParseHelper;

@FunctionalInterface
public interface FinalDefinitionCreator<P extends PreParseHelper, R extends Blueprint> {

    R create(P preParser);
}
