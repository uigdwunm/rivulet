package zly.rivulet.base.definition;

/**
 * Description 预解析后得到的信息
 *
 * @author zhaolaiyuan
 * Date 2021/12/11 9:43
 **/
public interface Definition {

    /**
     * Description 用于分析器进行分析，有些需要复制对象，有些可以用原对象（不可修改的 或者 已经是最小粒度的）
     *
     * @author zhaolaiyuan
     * Date 2022/3/27 11:33
     **/
    Definition forAnalyze();

}