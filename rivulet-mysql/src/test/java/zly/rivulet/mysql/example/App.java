package zly.rivulet.mysql.example;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import zly.rivulet.mysql.example.model.UserDO;
import zly.rivulet.mysql.example.model.UserJoinVO;
import zly.rivulet.mysql.example.model.UserType;
import zly.rivulet.sql.describer.query.QueryBuilder;
import zly.rivulet.sql.describer.query.desc.Mapping;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;

public class App {


    public static void main(String[] args) {
        UserDO userDO = new UserDO(1, "张小三", 18, true, UserType.VIP, new Date());

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserDO.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

                Parameter[] parameters = method.getParameters();
                Object result = methodProxy.invokeSuper(o, args);
                return result;
            }
        });

        QueryBuilder.query(UserDO.class, UserJoinVO.class).select(
            Mapping.of(UserJoinVO::setName, UserDO::getName),
            Mapping.of(UserJoinVO::setAge, UserDO::getAge)
        ).build();


    }

}
