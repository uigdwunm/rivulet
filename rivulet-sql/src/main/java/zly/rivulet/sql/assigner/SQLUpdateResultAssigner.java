package zly.rivulet.sql.assigner;

import zly.rivulet.base.assigner.Assigner;

public class SQLUpdateResultAssigner implements Assigner<Integer> {

    @Override
    public Object getValue(Integer results, int indexStart) {
        return results;
    }

    @Override
    public int size() {
        return 0;
    }
}