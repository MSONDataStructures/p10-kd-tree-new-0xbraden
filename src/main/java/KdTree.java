import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {

    // Node class for KD-Tree
    private static class Node {
        private Point2D p;      // point
        private RectHV rect;    // rectangle for this node
        private Node lb;        // left/bottom subtree
        private Node rt;        // right/top subtree
    }

    private static final int CANVAS_SIZE = 640;
    private static final double PEN_RADIUS = 0.01;

    private Node root;          // root node
    private int size;           // how many nodes we

    // Make empty tree
    public KdTree() {
        root = null;
        size = 0;
    }

    // Check if empty
    public boolean isEmpty() {
        return size == 0;
    }

    // How many points
    public int size() {
        return size;
    }

    // Adds point if it's not already there
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("cant use null point");
        }

        root = insert(root, p, true, new RectHV(0, 0, 1, 1));
    }

    // helper for insertion
    private Node insert(Node node, Point2D point, boolean vertical, RectHV rect) {
        if (node == null) {
            Node newNode = new Node();
            newNode.p = point;
            newNode.rect = rect;
            size++;
            return newNode;
        }

        if (node.p.equals(point)) {
            return node;
        }

        int cmp;
        if (vertical) {
            cmp = Double.compare(point.x(), node.p.x());
        } else {
            cmp = Double.compare(point.y(), node.p.y());
        }

        if (cmp < 0) {
            RectHV childRect;
            if (vertical) {
                childRect = new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
            } else {
                childRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
            }

            node.lb = insert(node.lb, point, !vertical, childRect);
        } else {
            RectHV childRect;
            if (vertical) {
                childRect = new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            } else {
                childRect = new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax());
            }

            node.rt = insert(node.rt, point, !vertical, childRect);
        }

        return node;
    }

    // Checks if the point is in the tree
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("kant search for null");
        }

        return contains(root, p, true);
    }

    // helper for contains
    private boolean contains(Node node, Point2D point, boolean vertical) {
        if (node == null) {
            return false;
        }

        if (node.p.equals(point)) {
            return true;
        }

        int cmp;
        if (vertical) {
            cmp = Double.compare(point.x(), node.p.x());
        } else {
            cmp = Double.compare(point.y(), node.p.y());
        }

        if (cmp < 0) {
            return contains(node.lb, point, !vertical);
        } else {
            return contains(node.rt, point, !vertical);
        }
    }

    // draws all points
    public void draw() {
        StdDraw.setCanvasSize(CANVAS_SIZE, CANVAS_SIZE);
        StdDraw.setPenRadius(PEN_RADIUS);

        draw(root, true);
    }

    // helper for drawing
    private void draw(Node node, boolean vertical) {
        if (node == null) {
            return;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(PEN_RADIUS);
        node.p.draw();

        StdDraw.setPenRadius();
        if (vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }

        draw(node.lb, !vertical);
        draw(node.rt, !vertical);
    }

    // Gets points inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("ts rectangle is null..");
        }

        ArrayList<Point2D> pointsInRange = new ArrayList<>();
        range(root, rect, pointsInRange);
        return pointsInRange;
    }

    // helper for range search
    private void range(Node node, RectHV rect, ArrayList<Point2D> pointsInRange) {
        if (node == null) {
            return;
        }

        if (!rect.intersects(node.rect)) {
            return;
        }

        if (rect.contains(node.p)) {
            pointsInRange.add(node.p);
        }

        range(node.lb, rect, pointsInRange);
        range(node.rt, rect, pointsInRange);
    }

    // Finds closest point to p
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("ts point kant be null");
        }

        if (isEmpty()) {
            return null;
        }

        return nearest(root, p, root.p, true);
    }

    // helper for nearest search
    private Point2D nearest(Node node, Point2D query, Point2D closest, boolean vertical) {
        if (node == null) {
            return closest;
        }

        double closestDist = query.distanceSquaredTo(closest);

        if (node.rect.distanceSquaredTo(query) > closestDist) {
            return closest;
        }

        double nodeDist = query.distanceSquaredTo(node.p);
        if (nodeDist < closestDist) {
            closest = node.p;
        }

        Node first, second;

        int cmp;
        if (vertical) {
            cmp = Double.compare(query.x(), node.p.x());
        } else {
            cmp = Double.compare(query.y(), node.p.y());
        }

        if (cmp < 0) {
            first = node.lb;
            second = node.rt;
        } else {
            first = node.rt;
            second = node.lb;
        }

        closest = nearest(first, query, closest, !vertical);
        closest = nearest(second, query, closest, !vertical);

        return closest;
    }

    // Testing stuff
    public static void main(String[] args) {
        KdTree tree = new KdTree();

        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));

        System.out.println("how many: " + tree.size());
        System.out.println("has (0.5, 0.4): " + tree.contains(new Point2D(0.5, 0.4)));
        System.out.println("has (0.1, 0.1): " + tree.contains(new Point2D(0.1, 0.1)));

        RectHV rect = new RectHV(0.1, 0.1, 0.6, 0.6);
        System.out.println("points in box: ");
        for (Point2D p : tree.range(rect)) {
            System.out.println(p);
        }

        Point2D query = new Point2D(0.6, 0.6);
        System.out.println("closest to " + query + ": " + tree.nearest(query));
    }
}