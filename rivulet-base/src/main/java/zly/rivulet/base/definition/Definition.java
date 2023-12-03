package zly.rivulet.base.definition;

/**
 * Description 预解析后得到的信息
 *
 * @author zhaolaiyuan
 * Date 2021/12/11 9:43
 **/
public interface Definition {

    /**
     * Description 复制器
     *
     * @author zhaolaiyuan
     * Date 2022/3/27 11:33
     **/
    Copier copier();

    interface Copier {
        Definition copy();
    }

    class ThisCopier<T extends Definition> implements Copier {
        private final T t;

        public ThisCopier(T t) {
            this.t = t;
        }

        @Override
        public Definition copy() {
            return t;
        }
    }


}