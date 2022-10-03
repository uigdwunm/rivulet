package zly.rivulet.base.utils;

import java.util.concurrent.ConcurrentHashMap;

public class TwofoldConcurrentHashMap<F, S, V> {
    private final ConcurrentHashMap<F, ConcurrentHashMap<S, V>> map;

    public TwofoldConcurrentHashMap(int initialCapacity) {
        map = new ConcurrentHashMap<>(initialCapacity);
    }

    public TwofoldConcurrentHashMap() {
        map = new ConcurrentHashMap<>();
    }

    public V get(F f, S s) {
        ConcurrentHashMap<S, V> svConcurrentHashMap = map.get(f);
        if (svConcurrentHashMap == null) {
            return null;
        } else {
            return svConcurrentHashMap.get(s);
        }
    }

    public void put(F f, S s, V v) {
        ConcurrentHashMap<S, V> svConcurrentHashMap = map.get(f);
        if (svConcurrentHashMap == null) {
            svConcurrentHashMap = new ConcurrentHashMap<>();
            map.put(f, svConcurrentHashMap);
        }
        svConcurrentHashMap.put(s, v);
    }
}
