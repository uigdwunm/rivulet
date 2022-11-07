package zly.rivulet.base.pipeline;

import zly.rivulet.base.generator.Fish;

@FunctionalInterface
public interface Executor {
    Object exec(Fish fish);
}
}
