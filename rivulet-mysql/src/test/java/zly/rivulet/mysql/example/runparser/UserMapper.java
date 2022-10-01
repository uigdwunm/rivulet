package zly.rivulet.mysql.example.runparser;

import zly.rivulet.base.definer.annotations.RivuletMapper;
import zly.rivulet.mysql.example.vo.UserVO;

public interface UserMapper {

    @RivuletMapper("sdf")
    UserVO select(long id);

}
