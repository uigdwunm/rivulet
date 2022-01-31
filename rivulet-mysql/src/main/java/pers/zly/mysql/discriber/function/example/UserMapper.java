package pers.zly.mysql.discriber.function.example;

import vkllyr.jpaminus.describer.annotation.Query;

public interface UserMapper {

    @Query("sdf")
    UserDO select(long id);

}
