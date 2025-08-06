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

public class BruteCollinearPoints {
    private ArrayList<LineSegment> lineSegments;
    private int numberOfLineSegments = 0;

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
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

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    for (int w = k + 1; w < n; w++) {
                        if (
                                copyPoints[i].slopeTo(copyPoints[j]) == copyPoints[i].slopeTo(
                                        copyPoints[k])
                                        && copyPoints[i].slopeTo(copyPoints[k])
                                        == copyPoints[i].slopeTo(
                                        copyPoints[w])
                        ) {
                            LineSegment line = new LineSegment(copyPoints[i], copyPoints[w]);
                            lineSegments.add(line);
                            numberOfLineSegments++;
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments()        // the number of line segments
    {
        return numberOfLineSegments;
    }

    public LineSegment[] segments()                // the line segments
    {
        LineSegment[] result = new LineSegment[lineSegments.size()];
        result = lineSegments.toArray(result);
        return result;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
