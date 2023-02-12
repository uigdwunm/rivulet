package zly.rivulet.base.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class View<T> implements Iterable<T> {

    private final Object[] elements;

    private View(Object[] elements) {
        this.elements = elements;
    }

    public int size() {
        return elements.length;
    }

    public static <T> View<T> create(T[] elements) {
        Object[] copy = new Object[elements.length];
        System.arraycopy(elements, 0, copy, 0, elements.length);
        return new View<>(copy);
    }

    public static <T> View<T> create(Collection<T> elements) {
        Object[] copy = new Object[elements.size()];
        System.arraycopy(elements.toArray(), 0, copy, 0, elements.size());
        return new View<>(copy);
    }

    public T get(int index) {
        return (T) elements[index];
    }

    public Stream<T> stream() {
        return (Stream<T>) Arrays.stream(elements);
    }

    @Override
    public Iterator<T> iterator() {
        return new ViewIterator();
    }

    private class ViewIterator implements Iterator<T> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return index != elements.length;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            return (T) elements[index++];
        }
    }
}
