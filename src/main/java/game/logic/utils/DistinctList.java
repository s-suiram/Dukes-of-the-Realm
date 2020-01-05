package game.logic.utils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;


/**
 * a list that does not allow duplicates like a set, but still allow list-specific operations.
 * is also as fast as a set on contains operations.
 * @param <T>
 */
public class DistinctList<T> extends ArrayList<T> {

    private Set<T> set = new HashSet<>();

    public DistinctList(DistinctList<? extends T> dl){
        super(dl);
        this.set = new HashSet<>(dl);
    }

    public DistinctList(int initialCapacity){
        super(initialCapacity);
    }

    public DistinctList(Collection<? extends T> c){
        super(c);
    }


    public DistinctList(){
        super();
    }

    public static <T> Collector<T,DistinctList<T>,DistinctList<T>> collector(){
        return Collector.of(DistinctList::new, DistinctList::add, (ts, ts2) -> { ts.addAll(ts2); return ts;});
    }

    @Override
    public boolean add(T t) {
        if(set.contains(t))
            return false;
        set.add(t);
        return super.add(t);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        removeAll(c);
        set.addAll(c);
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        removeAll(c);
        set.addAll(c);
        return super.addAll(index, c);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        set.removeIf(filter);
        return super.removeIf(filter);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        set.removeAll(this.subList(fromIndex,toIndex));
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public boolean remove(Object o) {
        set.remove(o);
        return super.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        set.removeAll(c);
        return super.removeAll(c);
    }

    @Override
    public T remove(int index) {
        set.remove(get(index));
        return super.remove(index);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        set.retainAll(c);
        return super.retainAll(c);
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }
}
