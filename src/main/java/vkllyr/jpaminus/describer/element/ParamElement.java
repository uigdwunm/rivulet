package vkllyr.jpaminus.describer.element;

/**
 * Description
 * 这部分用于描述以下几种位置的参数：
 * 1，select的字段，as左边
 * 2，where 符号左右两边都有可能
 * 3，having 左右
 * 这部分有三种可能
 * 1，常量，最终转成字符串
 * 2，子查询语句
 * 3，查询模型映射
 * 4,function
 *
 * @author zhaolaiyuan
 * Date 2021/11/28 11:48
 **/
public interface ParamElement {
}
