package zly.rivulet.sql.describer.param;

import zly.rivulet.base.describer.param.ParamCheckType;

/**
 * Description 拼接到语句中的形式
 *
 * @author zhaolaiyuan
 * Date 2022/2/26 9:51
 **/
public enum SqlParamCheckType implements ParamCheckType {

    // 占位符形式
    PLACEHOLDER,

    // 直接拼到语句中
    NATURE,

}
