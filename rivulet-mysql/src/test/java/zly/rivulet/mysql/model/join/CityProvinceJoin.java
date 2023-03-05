package zly.rivulet.mysql.model.join;

import zly.rivulet.mysql.model.CityDO;
import zly.rivulet.mysql.model.ProvinceDO;
import zly.rivulet.sql.definer.annotations.SQLSubQuery;
import zly.rivulet.sql.describer.condition.join.JoinCondition;
import zly.rivulet.sql.describer.join.ComplexDescriber;
import zly.rivulet.sql.describer.join.QueryComplexModel;
import zly.rivulet.sql.describer.query.SQLQueryMetaDesc;

/**
 * 联表对象需要继承QueryComplexModel
 **/
public class CityProvinceJoin implements QueryComplexModel {

    private CityDO cityDO;

    private CityDO cityDO2;

    @SQLSubQuery("queryProvince")
    private ProvinceDO subProvinceDO;

    private ProvinceDO provinceDO;

    @Override
    public ComplexDescriber register() {
        ComplexDescriber describer = ComplexDescriber.from(cityDO);

        // 联表条件
        describer.leftJoin(cityDO2)
            .on(
                JoinCondition.Equal.of(provinceDO::getCode, cityDO2::getProvinceCode)
            );
        // 联表条件
        describer.leftJoin(provinceDO)
            .on(
                JoinCondition.Equal.of(provinceDO::getCode, cityDO::getProvinceCode)
            );
        describer.leftJoin(subProvinceDO)
            .on(
                JoinCondition.Equal.of(subProvinceDO::getCode, cityDO::getProvinceCode)
            );

        return describer;
    }

    public CityDO getCityDO() {
        return cityDO;
    }

    public ProvinceDO getProvinceDO() {
        return provinceDO;
    }
}
