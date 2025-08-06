/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 * ref: https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> bst;

    public PointSET() { // construct an empty set of points
        bst = new SET<>();
    }

    public boolean isEmpty() { // is the set empty?
        return this.bst.isEmpty();
    }

    public int size() { // number of points in the set
        return this.bst.size();
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException();
        this.bst.add(p);
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null) throw new IllegalArgumentException();

        return this.bst.contains(p);
    }

    public void draw() { // draw all points to standard draw
        for (Point2D p : this.bst)
            p.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        ArrayList<Point2D> insides = new ArrayList<>();
        for (Point2D p : this.bst) {
            if (rect.contains(p))
                insides.add(p);
        }
        return insides;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (this.bst.isEmpty()) return null;
        Point2D nearest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Point2D target : this.bst) {
            double distance = p.distanceSquaredTo(target);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = target;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        Point2D point1 = new Point2D(0, 0);
        Point2D point2 = new Point2D(0.1, 0.4);
        Point2D point3 = new Point2D(0.6, 0.5);

        PointSET tree = new PointSET();
        tree.insert(point1);
        tree.insert(point2);
        tree.insert(point3);

        tree.draw();

        RectHV rect = new RectHV(0.4, 0.3, 0.8, 0.6);
        rect.draw();

        StdOut.println(tree.range(rect));
    } // unit testing of the methods (optional)
}
