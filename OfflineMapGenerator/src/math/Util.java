package math;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Ethan on 8/15/2016.
 */
public class Util {

    public static boolean pointInsidePolygon(Vector2d point, Vector2d[] polygon, BoundingBox bb) {
        double minX = bb.getMin().x;

        Vector2d scanStart = new Vector2d(Integer.MIN_VALUE, point.y);

        int numIntersects = 0;
        for (int i = 0; i < polygon.length; i++) {
            Vector2d start = polygon[i];
            Vector2d end = null;

            if (i + 1 < polygon.length) {
                end = polygon[i  + 1];
            } else {
                end = polygon[0];
            }

            if (isIntersectionSimple(scanStart, point, start, end)) {
                numIntersects++;
            }
        }

        if (numIntersects == 0) {
            return false;
        }
        if (numIntersects % 2 == 0) {
            return false;
        }
        return true;
    }


    //This is a really really really really REALLY crappy way to do line intersection. Even the
    //clockwise sorting is crappy... optimise this first. I cannot understate how crappy this is.
    //It might be the worst solution to this problem ever

    public static Vector2d getIntersection(Vector2d ps1, Vector2d pe1, Vector2d ps2, Vector2d pe2) {
        if (isIntersectionSimple(ps1, pe1, ps2, pe2)) {
            double a1 = pe1.y - ps1.y;
            double b1 = ps1.x - pe1.x;
            double c1 = a1 * ps1.x + b1 * ps1.y;

            double a2 = pe2.y - ps2.y;
            double b2 = ps2.x - pe2.x;
            double c2 = a2 * ps2.x + b2 * ps2.y;

            double delta = a1 * b2 - a2 * b1;

            if (delta == 0) {
                return null;
            }

            Vector2d point = new Vector2d((b2*c1 - b1*c2) / delta, (a1*c2 - a2*c1) / delta);
            return point;
        }

        return null;
    }

    //Convert to polar coordinates to do lazy sort
    public static boolean isIntersection(Vector2d ps1, Vector2d pe1, Vector2d ps2, Vector2d pe2) {
        Vector2d center = Vector2d.add(Vector2d.add(ps1, pe1), Vector2d.add(ps2, pe2));
        center.mul(0.25d);

        Vector2d ps1Ajusted = toPolar(Vector2d.sub(ps1, center));
        Vector2d pe1Ajusted = toPolar(Vector2d.sub(pe1, center));
        Vector2d ps2Ajusted = toPolar(Vector2d.sub(ps2, center));
        Vector2d pe2Ajusted = toPolar(Vector2d.sub(pe2, center));

        //Vector2d[] array = {ps1Ajusted, pe1Ajusted, ps2Ajusted, pe2Ajusted};

        Vector2d[] array = {ps1, pe1, ps2, pe2};
        Arrays.sort(array, new Comparator<Vector2d>() {
            @Override
            public int compare(Vector2d o1, Vector2d o2) {
                return vectorLess(o1, o2, center);
            }
        });

        /*Arrays.sort(array, new Comparator<Vector2d>() {
            @Override
            public int compare(Vector2d o1, Vector2d o2) {
                double thetaDelta = o1.y - o2.y;

                if (thetaDelta < 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }); */

        boolean output = false;
        if (array[0] == ps1) {
            output =  array[2] == pe1;
        }
        if (array[0] == pe1) {
            output =  array[2] == ps1;
        }
        if (array[0] == ps2) {
            output = array[2] == pe2;
        }
        if (array[0] == pe2) {
            output = array[2] == ps2;
        }
        System.out.println(output);
        return output;

        //throw new RuntimeException("Something's really broken");
    }

    public static boolean isIntersectionSimple(Vector2d a, Vector2d b, Vector2d c, Vector2d d) {
        return ccw(a, c, d) != ccw(b ,c, d) && ccw(a,b,c) != ccw(a,b,d);
    }

    private static boolean ccw(Vector2d a, Vector2d b, Vector2d c) {
        return (c.y-a.y) * (b.x-a.x) > (b.y-a.y) * (c.x-a.x);
    }

    public static int vectorLess(Vector2d a, Vector2d b, Vector2d cent) {
        if (a.x - cent.x >= 0 && b.x - cent.x < 0) {
            return -1;
        }
        if (a.x - cent.x < 0 && b.x - cent.x >= 0) {
            return 1;
        }
        if (a.x - cent.x == 0 && b.x - cent.x == 0) {
            if (a.y - cent.y >= 0 || b.y - cent.y >= 0) {
                return (int) Math.signum(a.y - b.y);
            }
            return (int) Math.signum(b.y - a.y);
        }

        double det = (a.x - cent.x) * (b.y - cent.y) - (b.x - cent.x) * (a.y - cent.y);
        if (det < 0) {
            return -1;
        }
        if (det > 0) {
            return 1;
        }

        double d1 = Vector2d.sub(a, cent).mag2();
        double d2 = Vector2d.sub(b, cent).mag2();

        return (int) Math.signum(d1 - d2);
    }

    public static Vector2d toPolar(Vector2d vec) {
        double r = Math.sqrt(vec.x * vec.x + vec.y * vec.y);
        double theta = Math.atan2(vec.y, vec.x);

        int ySign = (int)Math.signum(vec.y);


        return new Vector2d(r, Math.toDegrees(theta));
    }
}
