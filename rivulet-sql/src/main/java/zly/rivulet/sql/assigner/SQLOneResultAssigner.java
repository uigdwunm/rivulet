package zly.rivulet.sql.assigner;

import zly.rivulet.base.assigner.Assigner;
import zly.rivulet.base.convertor.ConvertorManager;
import zly.rivulet.base.convertor.ResultConvertor;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SQLOneResultAssigner implements Assigner<ResultSet> {

    private final Class<?> targetType;
    private final ConvertorManager convertorManager;

    public SQLOneResultAssigner(ConvertorManager convertorManager, Class<?> targetType) {
        this.targetType = targetType;
        this.convertorManager = convertorManager;
    }

    @Override
    public Object getValue(ResultSet results, int indexStart) {
        try {
            Object object = results.getObject(indexStart + 1);
            ResultConvertor resultConvertor = convertorManager.getResultConvertor(object.getClass(), targetType);
            return resultConvertor.convert(object);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        return 1;
    }
}
