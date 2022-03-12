package zly.rivulet.base.analyzer;

import zly.rivulet.base.preparser.Rock;
import zly.rivulet.base.runparser.Fish;

public interface Analyzer {

    Fish runTimeAnalyze(Fish statement);

    Rock preAnalyze(Rock rock);
}
