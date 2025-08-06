/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int N = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) (new Object[N]);
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            if (q[i] != null) copy[i] = q[i];
        }
        q = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (N == q.length) resize(2 * (1 + q.length));
        q[N++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniformInt(N);
        Item item = q[index];
        q[index] = q[N - 1];
        q[N - 1] = null;
        N--;
        if (N > 0 && N == q.length / 4) resize(q.length / 2);

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniformInt(N);
        return q[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomListIterator();
    }

    private class RandomListIterator implements Iterator<Item> {
        private int[] orders;
        private int i = 0;

        public RandomListIterator() {
            orders = new int[N];
            for (int j = 0; j < N; j++) orders[j] = j;
            StdRandom.shuffle(orders);
        }

        public boolean hasNext() {
            return i < N;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return q[orders[i++]];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }


    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);
        q.enqueue(5);
        StdOut.println("queue size: " + q.size());
        q.dequeue();
        q.dequeue();
        for (int i : q) StdOut.println(i);
        StdOut.println("queue size: " + q.size());
    }

}
