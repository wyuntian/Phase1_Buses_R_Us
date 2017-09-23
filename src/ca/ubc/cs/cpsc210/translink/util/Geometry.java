package ca.ubc.cs.cpsc210.translink.util;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Compute relationships between points, lines, and rectangles represented by LatLon objects
 */
public class Geometry {
    /**
     * Return true if the point is inside of, or on the boundary of, the rectangle formed by northWest and southeast
     * @param northWest         the coordinate of the north west corner of the rectangle
     * @param southEast         the coordinate of the south east corner of the rectangle
     * @param point             the point in question
     * @return                  true if the point is on the boundary or inside the rectangle
     */
    public static boolean rectangleContainsPoint(LatLon northWest, LatLon southEast, LatLon point) {
        double nwLat = northWest.getLatitude();
        double nwLon = northWest.getLongitude();
        double seLat = southEast.getLatitude();
        double seLon = southEast.getLongitude();
        double pLat = point.getLatitude();
        double pLon = point.getLongitude();

        if(between(seLat,nwLat,pLat) && between(nwLon, seLon,pLon)){
            return true;
        }

        return false;
    }

    /**
     * Return true if the rectangle intersects the line
     * @param northWest         the coordinate of the north west corner of the rectangle
     * @param southEast         the coordinate of the south east corner of the rectangle
     * @param src               one end of the line in question
     * @param dst               the other end of the line in question
     * @return                  true if any point on the line is on the boundary or inside the rectangle
     */
    public static boolean rectangleIntersectsLine(LatLon northWest, LatLon southEast, LatLon src, LatLon dst) {
        Point2D.Double p1 = new Point2D.Double(northWest.getLatitude(),northWest.getLongitude());
        Point2D.Double p2 = new Point2D.Double(northWest.getLatitude(),southEast.getLongitude());
        Point2D.Double p3 = new Point2D.Double(southEast.getLatitude(),northWest.getLongitude());
        Point2D.Double p4 = new Point2D.Double(southEast.getLatitude(),southEast.getLongitude());

        Point2D.Double p5 = new Point2D.Double(src.getLatitude(),src.getLongitude());
        Point2D.Double p6 = new Point2D.Double(dst.getLatitude(),dst.getLongitude());

        boolean intersects1 = Line2D.linesIntersect(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p5.getX(), p5.getY(), p6.getX(), p6.getY());
        boolean intersects2 = Line2D.linesIntersect(p1.getX(), p1.getY(), p3.getX(), p3.getY(), p5.getX(), p5.getY(), p6.getX(), p6.getY());
        boolean intersects3 = Line2D.linesIntersect(p2.getX(), p2.getY(), p4.getX(),p4.getY(),p5.getX(), p5.getY(), p6.getX(), p6.getY());
        boolean intersects4 = Line2D.linesIntersect(p3.getX(),p3.getY(),p4.getX(),p4.getY(),p5.getX(), p5.getY(), p6.getX(), p6.getY());


        if(intersects1 || intersects2 || intersects3 || intersects4 || shareAnyPoint(p1,p2,p5,p6) ||shareAnyPoint(p1,p3,p5,p6)
                || shareAnyPoint(p2,p4,p5,p6) || shareAnyPoint(p3,p4,p5,p6) || rectangleContainsPoint(northWest,southEast,src)
                || rectangleContainsPoint(northWest,southEast,dst)){
            return true;
        }
        return false;
    }

    public static boolean shareAnyPoint(Point2D.Double A,Point2D.Double B,Point2D.Double C,Point2D.Double D){
        if (isPointOnTheLine(A, B, C)) return true;
        else if (isPointOnTheLine(A, B, D)) return true;
        else if (isPointOnTheLine(C, D, A)) return true;
        else if (isPointOnTheLine(C, D, B)) return true;
        else return false;
    }

    public static boolean isPointOnTheLine(Point2D.Double A, Point2D.Double B, Point2D.Double P) {
        double m = (B.y - A.y) / (B.x - A.x);

        //handle special case where the line is vertical
        if (Double.isInfinite(m)) {
            if(A.x == P.x) return true;
            else return false;
        }

        if ((P.y - A.y) == m * (P.x - A.x)) return true;
        else return false;
    }

    /**
     * A utility method that you might find helpful in implementing the two previous methods
     * Return true if x is >= lwb and <= upb
     * @param lwb      the lower boundary
     * @param upb      the upper boundary
     * @param x         the value in question
     * @return          true if x is >= lwb and <= upb
     */
    private static boolean between(double lwb, double upb, double x) {
        return lwb <= x && x <= upb;
    }
}
