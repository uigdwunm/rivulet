package zly.rivulet.mysql.example.model;

import zly.rivulet.mysql.example.vo.UserVO;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.annotations.SQLModelJoin;
import zly.rivulet.sql.definer.annotations.SQLSubJoin;
import zly.rivulet.sql.describer.join.ComplexDescriber;
import zly.rivulet.sql.describer.query.condition.JoinCondition;

public class JoinQueryDO implements QueryComplexModel {

    @SQLModelJoin
    private UserDO userDO;

    @SQLModelJoin
    private CityDO cityDO;

    @SQLModelJoin
    private ProvinceDO provinceDO;

    @SQLSubJoin("sdf")
    private UserVO userFriend;

    @Override
    public ComplexDescriber register() {
        ComplexDescriber complexDescriber = ComplexDescriber.from(userDO);
        complexDescriber.leftJoin(cityDO).on(
            JoinCondition.equalTo(cityDO::getCode, userDO::getCityCode),
            JoinCondition.or(
                JoinCondition.equalTo(cityDO::getCode, userDO::getCityCode),
                JoinCondition.equalTo(cityDO::getCode, userDO::getCityCode)
            )
        );
        complexDescriber.leftJoin(provinceDO).on(JoinCondition.equalTo(cityDO::getProvinceCode, provinceDO::getCode));
        complexDescriber.leftJoin(userFriend).on(JoinCondition.equalTo(userFriend::getId, userDO::getFriendId));

        return complexDescriber;
    }

    public UserDO getUserDO() {
        return userDO;
    }

    public CityDO getCityDO() {
        return cityDO;
    }

    public ProvinceDO getProvinceDO() {
        return provinceDO;
    }
}
