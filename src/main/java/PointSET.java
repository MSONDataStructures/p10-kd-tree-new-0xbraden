import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {

    private static final int CANVAS_SIZE = 640;
    private static final double PEN_RADIUS = 0.01;

    // for storing points
    private final SET<Point2D> points;

    // Makes an empty set
    public PointSET() {
        points = new SET<>();
    }

    // Checks if empty
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // How many points we have
    public int size() {
        return points.size();
    }

    // adds a point if it's not already there
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("boaa null point :joy: :sob:");
        }

        points.add(p);
    }

    // Checks if the point exists
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("kant look for ts null point :sob:");
        }

        return points.contains(p);
    }

    // Draws all the points
    public void draw() {
        StdDraw.setCanvasSize(CANVAS_SIZE, CANVAS_SIZE);
        StdDraw.setPenRadius(PEN_RADIUS);
        StdDraw.setPenColor(StdDraw.BLACK);

        for (Point2D p : points) {
            p.draw();
        }
    }

    // Gets points inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("rectangle kant be null");
        }

        ArrayList<Point2D> pointsInRange = new ArrayList<>();

        for (Point2D p : points) {
            if (rect.contains(p)) {
                pointsInRange.add(p);
            }
        }

        return pointsInRange;
    }

    // Finds closest point to p
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("ts point kant be null");
        }

        if (isEmpty()) {
            return null;
        }

        Point2D nearest = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Point2D point : points) {
            double distance = p.distanceSquaredTo(point);

            if (distance < minDistance) {
                minDistance = distance;
                nearest = point;
            }
        }

        return nearest;
    }

    // Testing stuff
    public static void main(String[] args) {
        PointSET set = new PointSET();

        set.insert(new Point2D(0.2, 0.3));
        set.insert(new Point2D(0.5, 0.5));
        set.insert(new Point2D(0.7, 0.1));

        System.out.println("how many: " + set.size());
        System.out.println("has (0.5, 0.5): " + set.contains(new Point2D(0.5, 0.5)));
        System.out.println("has (0.1, 0.1): " + set.contains(new Point2D(0.1, 0.1)));

        RectHV rect = new RectHV(0.1, 0.1, 0.6, 0.6);
        System.out.println("points in box: ");
        for (Point2D p : set.range(rect)) {
            System.out.println(p);
        }

        Point2D query = new Point2D(0.6, 0.6);
        System.out.println("closest to " + query + ": " + set.nearest(query));
    }
}