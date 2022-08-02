package zly.rivulet.mysql.definer.annotations.type.json;

import zly.rivulet.base.convertor.Convertor;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.utils.Constant;
import zly.rivulet.mysql.definer.annotations.type.json.JsonDO;
import zly.rivulet.mysql.definer.annotations.type.string.MySQLVarchar;
import zly.rivulet.mysql.definer.outerType.VariableStringType;

/**
 * Description 可以对应String类型，也可以让模型继承{{@link JsonDO}}
 *
 * @author zhaolaiyuan
 * Date 2022/7/30 12:02
 **/
public @interface MySQLJson {

    class Type extends VariableStringType {
        public Type(MySQLVarchar mySQLInt) {
            super(mySQLInt.length());
        }

        public static void registerConvertors(ConvertorManager convertorManager) {
            convertorManager.register(new Convertor<String, Type>(String.class, Type.class) {
                @Override
                public String convertToJavaType(Object outerValue) {
                    // TODO
                    return null;
                }

                @Override
                public String convertToStatement(String innerValue) {
                    return Constant.apostrophe + innerValue + Constant.apostrophe;
                }
            });
        }
    }

}
