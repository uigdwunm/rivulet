package zly.rivulet.base.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Description 集合类型创建器(用于创建结果集时，如果是collection类型的容器，会在这里推断类型创建容器实例)
 *
 * @author zhaolaiyuan
 * Date 2022/9/27 8:36
 **/
public class CollectionInstanceCreator {

    private final Map<Class<?>, Supplier<?>> instanceCreatorMap = new ConcurrentHashMap<>();

    public CollectionInstanceCreator() {
        init();
    }

    private void init() {
        instanceCreatorMap.put(Collection.class, LinkedList::new);
        instanceCreatorMap.put(List.class, LinkedList::new);
        instanceCreatorMap.put(LinkedList.class, LinkedList::new);
        instanceCreatorMap.put(ArrayList.class, ArrayList::new);
        instanceCreatorMap.put(Set.class, HashSet::new);
        instanceCreatorMap.put(HashSet.class, HashSet::new);
    }

    public Collection<Object> create(Class<?> collectionType) {
        Supplier<?> supplier = instanceCreatorMap.get(collectionType);
        if (supplier != null) {
            return (Collection<Object>) supplier.get();
        }

        try {
            // 传入的类型找到无参构造方法
            Constructor<?> constructor = collectionType.getConstructor();
            supplier = () -> {
                try {
                    return constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };

            instanceCreatorMap.put(collectionType, supplier);
            return (Collection<Object>) supplier.get();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
