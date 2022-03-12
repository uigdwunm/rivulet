package zly.rivulet.base.utils;

/**
 * Description 父子关联翻转开关,可由子级影响父级，一旦子级影响父级翻转为false，将是不可逆的
 *
 * @author zhaolaiyuan
 * Date 2022/1/1 10:58
 **/
public class RelationSwitch {

    /**
     * 开关，为true时可用缓存，如果变成不可用，则是个不可逆的过程
     **/
    private boolean isEnable;

    /**
     * 如果子级缓存不可用那父级的也不可用
     **/
    private final RelationSwitch parentSwitch;

    private RelationSwitch(boolean isEnable, RelationSwitch parentSwitch) {
        this.isEnable = isEnable;
        this.parentSwitch = parentSwitch;

        if (!isEnable) {
            // 如果当前缓存是不可用的则要把父级缓存也失效掉
            parentSwitch.invalid();
        }
    }

    /**
     * Description 创建一个根开关，创建根开关时一般都不知道子级是否为true
     *
     * @author zhaolaiyuan
     * Date 2022/1/22 10:27
     **/
    public static RelationSwitch createRootSwitch() {
        return createRootSwitch(true);
    }

    public static RelationSwitch createRootSwitch(boolean isEnable) {
        return new RelationSwitch(isEnable, null);
    }

    /**
     * Description 一旦失效将不会再生效，不可逆的
     *
     * @author zhaolaiyuan
     * Date 2022/1/1 11:19
     **/
    public void invalid() {
        this.isEnable = false;
        // 父级也失效掉
        this.parentInvalid();
    }

    /**
     * Description 父级失效掉
     *
     * @author zhaolaiyuan
     * Date 2022/1/23 13:30
     **/
    public void parentInvalid() {
        if (parentSwitch != null) {
            parentSwitch.invalid();
        }
    }

    /**
     * Description 当前缓存是否可用
     *
     * @author zhaolaiyuan
     * Date 2022/1/1 11:02
     **/
    public boolean isEnable() {
        return this.isEnable;
    }

    /**
     * Description 生成新的子级按钮
     *
     * @author zhaolaiyuan
     * Date 2022/1/22 10:24
     **/
    public RelationSwitch subSwitch() {
        return new RelationSwitch(true, this);
    }

}
