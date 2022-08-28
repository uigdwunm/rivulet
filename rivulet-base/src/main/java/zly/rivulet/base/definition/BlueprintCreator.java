package zly.rivulet.base.definition;

import zly.rivulet.base.parser.toolbox.ParserPortableToolbox;

@FunctionalInterface
public interface BlueprintCreator<P extends ParserPortableToolbox, R extends Blueprint> {

    R create(P preParser);
}
