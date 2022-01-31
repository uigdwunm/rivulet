package vkllyr.jpaminus.definer.field.outerMate;

import vkllyr.jpaminus.utils.Constant;

public @interface VarcharColumn {

    // 列名称，必填
    String name();

    // 列长度，必填
    int length();

    // 列注释，非必填
    String desc() default Constant.EMPTY_STR;

    // 是否需要默认值，默认不需要
    boolean isDefault() default false;

    // 默认值，非必填，上面isDefault为true时这个值才会解析
    String defaultValue() default Constant.EMPTY_STR;


    class Info extends OuterMateInfo {

        private final int length;

        // 默认值，这里语义明确，为null时表示没设置过默认值
        private final String defalultValue;

        public Info(VarcharColumn varchar) {
            super(varchar.name(), varchar.desc());
            this.length = varchar.length();
            if (varchar.isDefault()) {
                // isDefault为true时才解析默认值
                defalultValue = varchar.defaultValue();
            } else {
                defalultValue = null;
            }
        }

        @Override
        public void check() {
            // TODO

        }

        @Override
        public String convertToStatement() {
            // TODO
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(super.getName());
            stringBuilder.append(' ');
            stringBuilder.append("VARCHER(").append(this.getLength()).append(")");
            if (this.defalultValue != null) {
                stringBuilder.append(" DEFAULT ");
                stringBuilder.append('\'').append(defalultValue).append('\'');
            }
            if (this.getDesc() != null) {
                stringBuilder.append(" COMMENT ");
                stringBuilder.append('\'').append(this.getDesc()).append('\'');
            }
            stringBuilder.append(';');
            return stringBuilder.toString();
        }

        public int getLength() {
            return length;
        }
    }
}
