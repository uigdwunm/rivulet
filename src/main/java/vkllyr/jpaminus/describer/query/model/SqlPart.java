package vkllyr.jpaminus.describer.query.model;

import vkllyr.jpaminus.utils.ArrayUtils;

public class SqlPart {

    private final String[] sqlPart;

    private final int fieldIndex;

    private final int[] paramIndexes;

    public SqlPart(String[] sqlPart, int fieldIndex, int[] paramIndexes) {
        this.sqlPart = sqlPart;
        this.fieldIndex = fieldIndex;
        this.paramIndexes = paramIndexes;
    }

    private void fillField(String field) {
        sqlPart[fieldIndex] = field;
    }

    private void fillParams(String[] params) {
        if (ArrayUtils.isEmpty(this.paramIndexes)) {
            // 无需填充
            return;
        }
        assert params != null && params.length == paramIndexes.length;
        int length = params.length;
        for (int i = 0; i < length; i++) {
            String param = params[i];
            int index = this.paramIndexes[i];

            this.sqlPart[index] = param;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : this.sqlPart) {
           stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }
}
