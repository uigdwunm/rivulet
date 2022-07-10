package zly.rivulet.mysql.definer;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.annotations.Comment;
import zly.rivulet.base.definer.annotations.DefaultValue;
import zly.rivulet.base.definer.outerType.OriginOuterType;
import zly.rivulet.base.exception.ModelDefineException;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.base.utils.View;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLBigInt;
import zly.rivulet.mysql.definer.annotations.type.numeric.MySQLInt;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLVarchar;
import zly.rivulet.sql.definer.SqlDefiner;
import zly.rivulet.sql.definer.annotations.SqlColumn;
import zly.rivulet.sql.definer.annotations.SqlTable;
import zly.rivulet.sql.definer.meta.SQLFieldMeta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MySQLDefiner extends SqlDefiner {


    private final Map<Class<?>, MySQLModelMeta> modelMetaMap = new HashMap<>();

    public MySQLDefiner(ConvertorManager convertorManager) {
        super(convertorManager);
    }

    @Override
    protected MySQLModelMeta parse(Class<?> clazz) {
        MySQLModelMeta modelMeta = modelMetaMap.get(clazz);
        if (modelMeta != null) {
            return modelMeta;
        }

        SqlTable sqlTable = clazz.getAnnotation(SqlTable.class);
        String tableName = sqlTable.value();

        Comment commentAnno = clazz.getAnnotation(Comment.class);
        String comment = commentAnno != null ? commentAnno.value() : null;

        List<MySQLFieldMeta> fieldMetaList = Arrays.stream(clazz.getDeclaredFields())
            .map(field -> this.parseFieldMeta(clazz, field))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        modelMeta = new MySQLModelMeta(
            tableName,
            clazz,
            View.create(fieldMetaList),
            comment
        );

        modelMetaMap.put(clazz, modelMeta);
        return modelMeta;
    }

    @Override
    protected void initTypeConvertor() {
        annotation_TypeCreator_Map.put(MySQLInt.class, anno -> new MySQLInt.Type((MySQLInt) anno));
        MySQLInt.Type.registerConvertors(super.convertorManager);

        annotation_TypeCreator_Map.put(MySQLVarchar.class, anno -> new MySQLVarchar.Type((MySQLVarchar) anno));
        MySQLVarchar.Type.registerConvertors(super.convertorManager);

        annotation_TypeCreator_Map.put(MySQLBigInt.class, anno -> new MySQLBigInt.Type((MySQLBigInt) anno));
        MySQLBigInt.Type.registerConvertors(super.convertorManager);
    }

    private MySQLFieldMeta parseFieldMeta(Class<?> clazz, Field field) {

        SqlColumn sqlColumn = null;
        String comment = null;
        String defaultValue = null;
        OriginOuterType originOuterType = null;
        for (Annotation annotation : field.getAnnotations()) {
            if (annotation instanceof SqlColumn) {
                sqlColumn = (SqlColumn) annotation;
            } else if (annotation instanceof Comment) {
                // 注释
                comment = ((Comment) annotation).value();
            } else if (annotation instanceof DefaultValue) {
                // 默认值
                defaultValue = ((DefaultValue) annotation).defaultValue();
            } else {
                Function<Annotation, OriginOuterType> originOuterTypeCreator = annotation_TypeCreator_Map.get(annotation.annotationType());
                if (originOuterTypeCreator != null) {
                    if (originOuterType != null) {
                        // 每个字段只能有一个类型
                        throw ModelDefineException.multiColumnType(clazz, field);
                    }
                    originOuterType = originOuterTypeCreator.apply(annotation);
                }
            }
        }
        if (sqlColumn == null) {
            // 没有这个注解说明不是表字段，不进行解析
            return null;
        }
        if (originOuterType == null) {
            // 没有类型
            throw ModelDefineException.loseType(clazz, field);
        }

        return new MySQLFieldMeta(
            field,
            StringUtil.defaultIfBlank(sqlColumn.value(), field.getName()),
            originOuterType,
            comment,
            defaultValue
        );
    }
}
