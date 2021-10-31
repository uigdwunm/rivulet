package vkllyr.jpaminus.definer.field.outerMate;

import vkllyr.jpaminus.utils.Constant;

public @interface VarcharColumn {

    // 列名称，必填
    String name();

    // 列长度，必填
    int length();

    // 列注释，非必填
    String desc() default Constant.EMPTY_STR;

    // 是否有一个空的默认值(由于java注解值不允许为null，所以对于空的情况要特殊判断)
    boolean isDefaultEmpty() default false;

    // 默认值，非必填
    String defaultValue() default Constant.EMPTY_STR;


    class Info extends OuterMateInfo {

        private final int length;

        // 默认值，这里语义明确，为null时表示没设置过默认值
        private final String defalultValue;

        public Info(VarcharColumn varchar) {
            super(varchar.name(), varchar.desc());
            this.length = varchar.length();
            if (varchar.isDefaultEmpty()) {
                // 如果明确有一个空字符串的默认值，则直接加上
                defalultValue = Constant.EMPTY_STR;
            } else if (Constant.EMPTY_STR.equals(varchar.defaultValue())) {
                // 明确没有空字符串的默认值，还是有空字符串，则认为没有设置过默认值
                defalultValue = null;
            } else {
                defalultValue = varchar.defaultValue();
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
