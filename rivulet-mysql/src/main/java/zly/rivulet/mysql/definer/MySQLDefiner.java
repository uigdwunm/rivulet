package zly.rivulet.mysql.definer;

import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.definer.annotations.Comment;
import zly.rivulet.base.definer.annotations.DefaultValue;
import zly.rivulet.base.definer.annotations.PrimaryKey;
import zly.rivulet.base.definer.outerType.OriginOuterType;
import zly.rivulet.base.exception.ModelDefineException;
import zly.rivulet.base.utils.StringUtil;
import zly.rivulet.base.utils.View;
import zly.rivulet.mysql.definer.annotations.type.binary.*;
import zly.rivulet.mysql.definer.annotations.type.date.*;
import zly.rivulet.mysql.definer.annotations.type.numeric.*;
import zly.rivulet.mysql.definer.annotations.type.string.*;
import zly.rivulet.sql.definer.SQLDefiner;
import zly.rivulet.sql.definer.annotations.SQLColumn;
import zly.rivulet.sql.definer.annotations.SQLTable;
import zly.rivulet.sql.exception.SQLModelDefineException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MySQLDefiner extends SQLDefiner {


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

        SQLTable sqlTable = clazz.getAnnotation(SQLTable.class);
        if (sqlTable == null) {
            throw SQLModelDefineException.notTable(clazz);
        }
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

//        SQLDefaultConvertor.registerDefault(convertorManager);

        // binary
        annotation_TypeCreator_Map.put(MySQLBinary.class, anno -> new MySQLBinary.Type((MySQLBinary) anno));
        annotation_TypeCreator_Map.put(MySQLBlob.class, anno -> new MySQLBlob.Type((MySQLBlob) anno));
        annotation_TypeCreator_Map.put(MySQLLongBlob.class, anno -> new MySQLLongBlob.Type((MySQLLongBlob) anno));
        annotation_TypeCreator_Map.put(MySQLMediumBlob.class, anno -> new MySQLMediumBlob.Type((MySQLMediumBlob) anno));
        annotation_TypeCreator_Map.put(MySQLTinyBlob.class, anno -> new MySQLTinyBlob.Type((MySQLTinyBlob) anno));

        // date
        annotation_TypeCreator_Map.put(MySQLDate.class, anno -> new MySQLDate.Type((MySQLDate) anno));
        annotation_TypeCreator_Map.put(MySQLDatetime.class, anno -> new MySQLDatetime.Type((MySQLDatetime) anno));
        annotation_TypeCreator_Map.put(MySQLTime.class, anno -> new MySQLTime.Type((MySQLTime) anno));
        annotation_TypeCreator_Map.put(MySQLTimestamp.class, anno -> new MySQLTimestamp.Type((MySQLTimestamp) anno));
        annotation_TypeCreator_Map.put(MySQLYear.class, anno -> new MySQLYear.Type((MySQLYear) anno));

        // int
        annotation_TypeCreator_Map.put(MySQLBigInt.class, anno -> new MySQLBigInt.Type((MySQLBigInt) anno));
        annotation_TypeCreator_Map.put(MySQLDecimal.class, anno -> new MySQLDecimal.Type((MySQLDecimal) anno));
        annotation_TypeCreator_Map.put(MySQLDouble.class, anno -> new MySQLDouble.Type((MySQLDouble) anno));
        annotation_TypeCreator_Map.put(MySQLFloat.class, anno -> new MySQLFloat.Type((MySQLFloat) anno));
        annotation_TypeCreator_Map.put(MySQLInt.class, anno -> new MySQLInt.Type((MySQLInt) anno));
        annotation_TypeCreator_Map.put(MySQLMediumInt.class, anno -> new MySQLMediumInt.Type((MySQLMediumInt) anno));
        annotation_TypeCreator_Map.put(MySQLSmallInt.class, anno -> new MySQLSmallInt.Type((MySQLSmallInt) anno));
        annotation_TypeCreator_Map.put(MySQLTinyInt.class, anno -> new MySQLTinyInt.Type((MySQLTinyInt) anno));

        // string
        annotation_TypeCreator_Map.put(MySQLChar.class, anno -> new MySQLChar.Type((MySQLChar) anno));
        annotation_TypeCreator_Map.put(MySQLJson.class, anno -> new MySQLJson.Type((MySQLJson) anno));
        annotation_TypeCreator_Map.put(MySQLLongText.class, anno -> new MySQLLongText.Type((MySQLLongText) anno));
        annotation_TypeCreator_Map.put(MySQLMediumText.class, anno -> new MySQLMediumText.Type((MySQLMediumText) anno));
        annotation_TypeCreator_Map.put(MySQLText.class, anno -> new MySQLText.Type((MySQLText) anno));
        annotation_TypeCreator_Map.put(MySQLTinyText.class, anno -> new MySQLTinyText.Type((MySQLTinyText) anno));
        annotation_TypeCreator_Map.put(MySQLVarchar.class, anno -> new MySQLVarchar.Type((MySQLVarchar) anno));
    }

    private MySQLFieldMeta parseFieldMeta(Class<?> clazz, Field field) {

        SQLColumn sqlColumn = null;
        String comment = null;
        String defaultValue = null;
        OriginOuterType originOuterType = null;
        boolean isPrimary = false;
        for (Annotation annotation : field.getAnnotations()) {
            if (annotation instanceof SQLColumn) {
                sqlColumn = (SQLColumn) annotation;
            } else if (annotation instanceof PrimaryKey) {
                isPrimary = true;
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
            defaultValue,
            isPrimary
        );
    }
}
