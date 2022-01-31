package vkllyr.jpaminus.describer.query.model;

import vkllyr.jpaminus.describer.query.definition.JoinOption;

import java.util.Arrays;
import java.util.List;

/**
 * Description 连表查询时要继承这个
 *
 * @author zhaolaiyuan
 * Date 2021/11/27 12:51
 **/
public class QueryJoin {

    private JoinOption[] joinOptions;

    protected void register(JoinOption ... joinOptions) {
        this.joinOptions = joinOptions;
    }

    public List<JoinOption> getJoinOptions() {
        return Arrays.asList(joinOptions);
    }
}
