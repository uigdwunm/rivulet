package zly.rivulet.mysql.example.runparser;

import zly.rivulet.base.definer.annotations.RivuletQueryMapper;
import zly.rivulet.mysql.example.vo.UserVO;

public interface UserMapper {

    @RivuletQueryMapper("sdf")
    UserVO select(long id);

}
