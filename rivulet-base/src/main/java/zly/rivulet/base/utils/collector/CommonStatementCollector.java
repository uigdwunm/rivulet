package zly.rivulet.base.utils.collector;

import zly.rivulet.base.generator.statement.Statement;
import zly.rivulet.base.utils.Constant;

public class CommonStatementCollector implements StatementCollector {
    
    private StringBuilder sb = new StringBuilder();
    
    @Override
    public StatementCollector leftBracket() {
        sb.append(Constant.LEFT_BRACKET);
        return this;
    }

    @Override
    public StatementCollector rightBracket() {
        sb.append(Constant.RIGHT_BRACKET);
        return this;
    }

    @Override
    public StatementCollector space() {
        sb.append(Constant.SPACE);
        return this;
    }

    @Override
    public StatementCollector append(Object o) {
        sb.append(o);
        return this;
    }

    @Override
    public StatementCollector append(float f) {
        sb.append(f);
        return this;
    }

    @Override
    public StatementCollector append(double d) {
        sb.append(d);
        return this;
    }

    @Override
    public StatementCollector append(long l) {
        sb.append(l);
        return this;
    }

    @Override
    public StatementCollector append(int i) {
        sb.append(i);
        return this;
    }

    @Override
    public StatementCollector append(char c) {
        sb.append(c);
        return this;
    }

    @Override
    public StatementCollector append(boolean b) {
        sb.append(b);
        return this;
    }

    @Override
    public StatementCollector append(char[] chars) {
        sb.append(chars);
        return this;
    }

    @Override
    public StatementCollector append(String str) {
        sb.append(str);
        return this;
    }

    @Override
    public StatementCollector append(Statement statement) {
        statement.collectStatement(this);
        return this;
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
