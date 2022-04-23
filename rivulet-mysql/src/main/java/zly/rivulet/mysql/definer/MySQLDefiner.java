package zly.rivulet.mysql.definer;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.Definer;
import zly.rivulet.base.definer.ModelMeta;
import zly.rivulet.base.definer.outerType.OriginOuterType;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLInt;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLVarchar;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MySQLDefiner extends Definer {

    /**
     * 注解和类型对象的映射
     **/
    private final Map<Class<?>, Function<Annotation, OriginOuterType>> annotation_TypeCreator_Map = new HashMap<>();

    public MySQLDefiner(ConvertorManager convertorManager) {
        super(convertorManager);
    }

    @Override
    protected ModelMeta parse(Class<?> clazz) {
        return null;
    }

    @Override
    protected void initTypeConvertor(ConvertorManager convertorManager) {
        annotation_TypeCreator_Map.put(MySQLInt.class, anno -> new MySQLInt.Type((MySQLInt) anno));
        MySQLInt.Type.registerConvertors(convertorManager);

        annotation_TypeCreator_Map.put(MySQLVarchar.class, anno -> new MySQLVarchar.Type((MySQLVarchar) anno));
        MySQLVarchar.Type.registerConvertors(convertorManager);

    }

}
