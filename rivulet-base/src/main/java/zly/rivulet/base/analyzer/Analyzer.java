package zly.rivulet.base.analyzer;

import zly.rivulet.base.definition.FinalDefinition;
import zly.rivulet.base.runparser.Fish;

public interface Analyzer {

    Fish runTimeAnalyze(Fish statement);

    FinalDefinition preAnalyze(FinalDefinition rock);
}
