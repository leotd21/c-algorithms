/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 * https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/RedBlackBST.html
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;

    private Node root;  // root of the BST

    // BST helper Node data type
    private class Node {
        private final Point2D key;            // key separator point
        private Node left, right;       // links to left and right subtrees
        private int size;               // subtree count
        private int axis;
        private final RectHV rect;      // rectangle

        public Node(Point2D p, int size, int axis, RectHV rect) {
            this.key = p;
            this.size = size;
            this.rect = rect;
            this.axis = axis;
        }

        public int getAxis() {
            return this.axis;
        }
    }

    // Initialize an empty kdtree
    public KdTree() {
    }

    // Node helper methods

    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    // number of points in the set
    public int size() {
        if (isEmpty()) return 0;
        return this.root.size;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    /*
    Standard BST search
     */
    private Point2D get(Point2D key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        return get(this.root, key);
    }

    // value associated with the given key in subtree rooted at x; null if no such key
    private Point2D get(Node h, Point2D key) {
        while (h != null) {
            if (key.equals(h.key)) return key;

            int cmp = compare(h, key);

            if (cmp <= 0) {
                h = h.left;
            }
            else { // if (cmp > 0)
                h = h.right;
            }
        }
        return null;
    }


    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument to contains() is null");
        // StdOut.println(get(p));
        return get(p) != null;
    }

    /*
    Kd Insertions
     */

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        // StdOut.println("Inserting point " + p);
        if (p == null) throw new IllegalArgumentException("argument to insert() is null");
        root = put(root, p, VERTICAL, 0, 0, 1, 1);
    }

    // insert key to subtree rooted at h
    private Node put(Node h, Point2D key, int axis, double xmin, double ymin, double xmax,
                     double ymax) {
        if (h == null) return new Node(key, 1, axis, new RectHV(xmin, ymin, xmax, ymax));

        int cmp = compare(h, key);

        if (cmp <= 0) {
            if (!key.equals(h.key)) {
                if (h.axis == VERTICAL)
                    h.left = put(h.left, key, 1 - axis, xmin, ymin, h.key.x(), ymax);
                else
                    h.left = put(h.left, key, 1 - axis, xmin, ymin, xmax,
                                 h.key.y());      // horizontal
            }
        }
        else { // if (cmp > 0)
            if (h.axis == VERTICAL)
                h.right = put(h.right, key, 1 - axis, h.key.x(), ymin, xmax, ymax);
            else
                h.right = put(h.right, key, 1 - axis, xmin, h.key.y(), xmax, ymax);
        }

        h.size = 1 + size(h.left) + size(h.right);

        return h;
    }

    /*
    Ordered symbol table
     */
    private Iterable<Node> nodes() {
        if (isEmpty()) throw new IllegalArgumentException("call nodes() on empty tree");

        Queue<Node> queue = new Queue<>();
        inorder(root, queue);
        return queue;
    }

    private void inorder(Node x, Queue<Node> queue) {
        if (x == null) return;

        inorder(x.left, queue);
        queue.enqueue(x);
        inorder(x.right, queue);
    }

    // draw all points to standard draw
    public void draw() {
        for (Node node : nodes()) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.02);
            Point2D key = node.key;
            key.draw();
            // StdOut.println(node.rect());
            StdDraw.setPenRadius(0.002);
            StdDraw.setPenColor(StdDraw.BLUE);
            if (node.axis == VERTICAL)
                StdDraw.setPenColor(StdDraw.RED);
            double xmin, ymin, xmax, ymax;
            if (node.axis == VERTICAL) {
                xmin = key.x();
                xmax = key.x();
                ymin = node.rect.ymin();
                ymax = node.rect.ymax();
            }
            else { // horizontal
                xmin = node.rect.xmin();
                ymin = key.y();
                xmax = node.rect.xmax();
                ymax = key.y();
            }
            StdDraw.line(xmin, ymin, xmax, ymax);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV query) {
        if (query == null) throw new IllegalArgumentException("arguement to range() query is null");

        ArrayList<Point2D> points = new ArrayList<>();
        rangeSearch(root, query, points);

        return points;
    }

    private void rangeSearch(Node h, RectHV query, ArrayList<Point2D> points) {
        if (h == null) return;

        if (query.contains(h.key)) points.add(h.key);

        if (h.left != null && h.left.rect.intersects(query)) {
            rangeSearch(h.left, query, points);
        }
        if (h.right != null && h.right.rect.intersects(query)) {
            rangeSearch(h.right, query, points);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument to nearest() is null");
        if (isEmpty()) return null;

        double shortest = root.key.distanceSquaredTo(p);

        return nearest(root, p, root.key, shortest);
    }

    private int compare(Node h, Point2D query) {
        int cmp;
        if (h.axis == VERTICAL) cmp = Double.compare(query.x(), h.key.x());
        else cmp = Double.compare(query.y(), h.key.y());                    // HORIZONTAL
        return cmp;
    }

    // nearest point from query point to subtree rooted at h
    private Point2D nearest(Node h, Point2D query, Point2D champion, double shortest) {
        if (h == null) return champion;     // if root is null, return current champion
        // StdOut.println("Visiting " + h.key);
        double distanceToPlane = h.rect.distanceSquaredTo(query);
        if (shortest < distanceToPlane) {
            return champion;
        }

        double distance = query.distanceSquaredTo(h.key);
        if (distance < shortest) {
            shortest = distance;
            champion = h.key;
            if (distance == 0.0) return champion;
        }
        int cmp = compare(h, query);

        Node nearer, farther;
        if (cmp <= 0) {
            nearer = h.left;
            farther = h.right;
        }
        else { // (cmp > 0)
            nearer = h.right;
            farther = h.left;
        }
        champion = nearest(nearer, query, champion, shortest);
        shortest = champion.distanceSquaredTo(query);

        if (distanceToPlane < shortest) {
            champion = nearest(farther, query, champion, shortest);
            // shortest = champion.distanceSquaredTo(query);
        }


        return champion;
    }

    // unit testing of the methods
    public static void main(String[] args) {

        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }
        Point2D query = new Point2D(0.5, 0.75);
        Point2D queryPoint = new Point2D(0.519, 0.908);

        StdOut.println("brute");
        StdOut.println(brute.size());
        StdOut.println(brute.contains(query));
        StdOut.println("Nearest point: " + brute.nearest(queryPoint));

        StdOut.println("\nkdtree");
        StdOut.println(kdtree.size());
        StdOut.println(kdtree.contains(query));
        StdOut.println("Nearest point: " + kdtree.nearest(queryPoint));

        kdtree.draw();
        RectHV rangeQuery = new RectHV(0.5, 0.5, 0.625, 0.875);
        kdtree.range(rangeQuery);
        rangeQuery.draw();
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.01);
        queryPoint.draw();

    }
}
