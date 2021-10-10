package vkllyr.jpaminus.describer.query.model;

import vkllyr.jpaminus.describer.query.desc.JoinOption;

import java.util.Arrays;
import java.util.List;

public class QueryJoin implements QueryBase {

    private JoinOption[] joinOptions;

    protected void register(JoinOption ... joinOptions) {
        this.joinOptions = joinOptions;
    }

    public List<JoinOption> getJoinOptions() {
        return Arrays.asList(joinOptions);
    }
}
