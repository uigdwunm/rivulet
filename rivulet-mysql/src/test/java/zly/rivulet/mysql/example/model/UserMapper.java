package zly.rivulet.mysql.example.model;

import zly.rivulet.base.definer.annotations.RivuletQueryMapper;

public interface UserMapper {

    @RivuletQueryMapper("sdf")
    UserDO select(long id);

}
