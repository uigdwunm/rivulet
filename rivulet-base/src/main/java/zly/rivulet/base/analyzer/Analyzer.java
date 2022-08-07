package zly.rivulet.base.analyzer;

import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.assembly_line.Fish;

public interface Analyzer {

    Fish runTimeAnalyze(Fish statement);

    Blueprint preAnalyze(Blueprint rock);
}
