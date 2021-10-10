package vkllyr.jpaminus.example;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import vkllyr.jpaminus.describer.query.builder.QueryBuilder;
import vkllyr.jpaminus.describer.query.builder.SelectBuilder;
import vkllyr.jpaminus.describer.query.desc.SimpleQueryDesc;
import vkllyr.jpaminus.describer.query.desc.WhereOption;

import java.lang.reflect.Method;
import java.util.Date;

public class App {

    SelectBuilder<UserDO> from = QueryBuilder.from(UserDO.class);

    public static void main(String[] args) {
        UserDO userDO = new UserDO(1, "张小三", 18, true, UserType.VIP, new Date());

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserDO.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

                Object result = methodProxy.invokeSuper(o, args);
                return result;
            }
        });

        SimpleQueryDesc<UserDO> build = QueryBuilder.from(UserDO.class)
            .build();


    }

}
