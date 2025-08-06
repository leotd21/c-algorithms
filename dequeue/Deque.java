/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first, last;
    private int N = 0;

    private class Node<Item> {
        Item item;
        Node<Item> next;
        Node<Item> prev;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        // return first == null;
        return N == 0;
    }

    // return the number of items on the deque
    public int size() {
        return N;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node<Item> oldFirst = first;
        first = new Node<Item>();
        first.item = item;
        if (isEmpty()) last = first;
        else {
            first.next = oldFirst;
            oldFirst.prev = first;
        }
        N++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node<Item> oldLast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else {
            oldLast.next = last;
            last.prev = oldLast;
        }
        N++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();

        Item item = first.item;
        first = first.next;
        N--;
        if (isEmpty()) last = null;
        else first.prev = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();

        Item item = last.item;
        last = last.prev;
        N--;
        if (isEmpty()) {
            first = null;
        }
        else last.next = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node<Item> current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> d = new Deque<String>();
        StdOut.println("d size: " + d.size());
        d.addFirst("a");
        d.removeFirst();
        StdOut.println("d size: " + d.size());
        d.addLast("l");
        d.removeLast();
        StdOut.println("d size: " + d.size());
        d.addFirst("b");
        d.addFirst("c");
        d.addLast("q");
        d.addLast("w");
        d.addLast("t");
        for (String s : d) {
            StdOut.println(s);
        }
        StdOut.println(d.size());
        d.removeFirst();
        d.removeLast();
        StdOut.println(d.size());
        for (String s : d) {
            StdOut.println(s);
        }
        try {
            d.removeLast();
            d.removeLast();
            d.removeLast();
            d.removeLast();
            d.removeLast();
            d.removeLast();
        }
        catch (NoSuchElementException e) {
            StdOut.println(e);
        }
        try {
            d.removeFirst();
            d.removeFirst();
            d.removeFirst();
            d.removeFirst();
            d.removeFirst();
            d.removeFirst();
        }
        catch (NoSuchElementException e) {
            StdOut.println(e);
        }

    }

}
