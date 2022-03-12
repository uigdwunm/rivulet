package zly.rivulet.sql.definer;

import zly.rivulet.base.definer.DefinerManager;
import zly.rivulet.base.definer.ModelMeta;

import java.lang.annotation.Annotation;

public class SqlDefinerManager implements DefinerManager {

    @Override
    public ModelMeta getModelMeta(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        return null;
    }
}
