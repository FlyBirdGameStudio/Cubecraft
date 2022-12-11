package io.flybird.util.container;

import java.util.*;

/**
 * array queue
 * <p>work with inject value from header,poll from last</p>
 *
 * @param <E> container Template class.
 * @author GrassBlock2022
 */
public class ArrayQueue<E> {
    public ArrayList<E> list = new ArrayList<>();

    /**
     * get size of container.
     *
     * @return size
     * @see ArrayList
     */
    public int size() {
        return this.list.size();
    }

    /**
     * get is empty from list.
     *
     * @return is empty.
     * @see ArrayList
     */
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    /**
     * get if contains object.
     *
     * @param e element
     * @return get if contains.
     * @see ArrayList
     */
    public boolean contains(E e) {
        return this.list.contains(e);
    }

    /**
     * generate an array of element.
     *
     * @param a array sample.
     * @return get array.
     * @see ArrayList
     */
    public E[] toArray(E[] a) {
        return this.list.toArray(a);
    }


    /**
     * add an object,inject in head.
     *
     * @param e element
     */
    public void add(E e) {
        if (this.list.isEmpty()) {
            this.list = new ArrayList<>();
        }
        if (e != null && !contains(e)) {
            this.list.add(0, e);
        }
    }

    /**
     * add some object,by order.
     *
     * @param all list of elements.
     */
    public void addAll(List<E> all) {
        for (E e : all) {
            this.add(e);
        }
    }

    /**
     * poll an object from ending,return null if array is empty
     *
     * @return element
     */
    public E poll() {
        try {
            if (list.size() > 0) {
                return this.list.remove(this.list.size() - 1);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * poll all from list,by order.
     *
     * @param count count
     * @return list.
     */
    public List<E> pollAll(int count) {
        ArrayList<E> returns = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            E e = this.poll();
            if (e != null) {
                returns.add(e);
            }
        }
        return returns;
    }

    /**
     * clear map.
     */
    public void clear() {
        this.list.clear();
    }

    /**
     * poll object by order,by prediction
     *
     * @param count  item count.
     * @param pollIf prediction
     * @return item by order.
     */
    public List<E> pollIf(int count, Prediction<E> pollIf) {
        ArrayList<E> returns = new ArrayList<>(1);
        for (E e : this.list) {
            if (pollIf.If(e)) {
                returns.add(e);
            }//给我standby到满足条件
            if (returns.size() >= count) {
                break;
            }
        }

        return returns;
    }

    /**
     * remove by prediction
     *
     * @param tester prediction
     * @see ArrayList
     */
    public void removeIf(Prediction<E> tester) {
        this.list.removeIf(tester::If);
    }

    /**
     * remove element
     *
     * @param e element
     * @see ArrayList
     */
    public void remove(E e) {
        this.list.remove(e);
    }

    /**
     * sort arraylist
     *
     * @param sorter sorter
     * @see ArrayList
     */
    public void sort(Comparator<E> sorter) {
        this.list.sort(sorter);
    }

    /**
     * prediction for test
     * @param <E>
     */
    public interface Prediction<E> {
        boolean If(E e);
    }
}
