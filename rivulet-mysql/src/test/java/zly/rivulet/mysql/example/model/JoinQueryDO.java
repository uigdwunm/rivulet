package zly.rivulet.mysql.example.model;

import zly.rivulet.mysql.example.model.address.City;
import zly.rivulet.mysql.example.model.address.Province;
import zly.rivulet.mysql.example.model.user.User;
import zly.rivulet.mysql.example.vo.UserVO;
import zly.rivulet.sql.definer.QueryComplexModel;
import zly.rivulet.sql.definer.annotations.SQLSubQuery;
import zly.rivulet.sql.describer.condition.JoinCondition;
import zly.rivulet.sql.describer.join.ComplexDescriber;

public class JoinQueryDO implements QueryComplexModel {

    private User user;

    private City city;

    private Province province;

    @SQLSubQuery("sdf")
    private UserVO userFriend;

    @Override
    public ComplexDescriber register() {
        ComplexDescriber complexDescriber = ComplexDescriber.from(user);
        complexDescriber.leftJoin(city).on(
            JoinCondition.equalTo(city::getCode, user::getCityCode),
            JoinCondition.or(
                JoinCondition.equalTo(city::getCode, user::getCityCode),
                JoinCondition.equalTo(city::getCode, user::getCityCode)
            )
        );
        complexDescriber.leftJoin(province).on(JoinCondition.equalTo(city::getProvinceCode, province::getCode));

        return complexDescriber;
    }

    public User getUserDO() {
        return user;
    }

    public City getCityDO() {
        return city;
    }

    public Province getProvinceDO() {
        return province;
    }
}
