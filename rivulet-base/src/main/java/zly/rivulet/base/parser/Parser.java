package zly.rivulet.base.parser;

import zly.rivulet.base.definer.Definer;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definition.Blueprint;
import zly.rivulet.base.describer.WholeDesc;

public interface Parser {

    Blueprint parse(WholeDesc wholeDesc);

    void addAnalyzer(Analyzer analyzer);

    Definer getDefiner();
}
