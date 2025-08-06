/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lineSegments;
    private int numberOfLineSegments = 0;

    public FastCollinearPoints(
            Point[] points)     // finds all line segments containing 4 or more points
    {
        if (points == null) throw new IllegalArgumentException();

        int n = points.length;

        Point[] copyPoints = new Point[n];
        for (int i = 0; i < n; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            copyPoints[i] = points[i];
        }
        Arrays.sort(copyPoints);


        for (int i = 1; i < n; i++) {
            if (copyPoints[i].compareTo(copyPoints[i - 1]) == 0)
                throw new IllegalArgumentException();
        }

        lineSegments = new ArrayList<>();

        for (Point p : points) {
            Arrays.sort(copyPoints);
            Arrays.sort(copyPoints, p.slopeOrder());

            int j = 2;
            while (j < n) {
                int cnt = 0;
                boolean duplicate = false;
                while (j < n && (p.slopeTo(copyPoints[j - 1]) == p.slopeTo(copyPoints[j]))) {
                    if (copyPoints[j - 1].compareTo(p) < 0) {
                        duplicate = true;
                    }
                    cnt++;
                    j++;
                }
                if (cnt > 1 && !duplicate) {
                    LineSegment line = new LineSegment(p, copyPoints[j - 1]);
                    lineSegments.add(line);
                    numberOfLineSegments++;
                }
                if (cnt == 0) j++;
            }
        }
    }

    public int numberOfSegments()        // the number of line segments
    {
        return numberOfLineSegments;
    }


    public LineSegment[] segments()                // the line segments
    {
        LineSegment[] results = new LineSegment[lineSegments.size()];
        lineSegments.toArray(results);
        return results;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

        StdOut.println("Number of LineSegments: " + collinear.numberOfLineSegments);
    }

}
