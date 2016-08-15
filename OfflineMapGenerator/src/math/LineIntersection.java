package math;

import map.MapArea;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Ethan on 8/15/2016.
 */
public class LineIntersection {
    //This is a really really really really REALLY crappy way to do line intersection. Even the
    //clockwise sorting is crappy... optimise this first. I cannot understate how crappy this is.
    //It might be the worst solution to this problem ever

    public static Vector2d getIntersection(Vector2d ps1, Vector2d pe1, Vector2d ps2, Vector2d pe2) {
        if (isIntersection(ps1, pe1, ps2, pe2)) {
            double a1 = pe1.y - ps1.y;
            double b1 = ps1.x - pe1.x;
            double c1 = a1 * ps1.x + b1 * ps1.y;

            double a2 = pe2.y - ps2.y;
            double b2 = ps2.x - pe2.x;
            double c2 = a2 * ps2.x + a2 * ps2.y;

            double delta = a1 * b2 - a2 * b1;

            if (delta == 0) {
                return null;
            }

            Vector2d point = new Vector2d((b2*c1 - b1*c2) / delta, (a1*c2 - a2*c1) / delta);
            return point;
        }

        return null;
    }

    public static boolean isIntersection(Vector2d ps1, Vector2d pe1, Vector2d ps2, Vector2d pe2) {
        Vector2d center = Vector2d.add(Vector2d.add(ps1, pe1), Vector2d.add(ps2, pe2));
        center.mul(0.25d);

        Vector2d ps1Ajusted = toPolar(Vector2d.sub(ps1, center));
        Vector2d pe1Ajusted = toPolar(Vector2d.sub(pe1, center));
        Vector2d ps2Ajusted = toPolar(Vector2d.sub(ps2, center));
        Vector2d pe2Ajusted = toPolar(Vector2d.sub(pe2, center));

        Vector2d[] array = {ps1Ajusted, pe1Ajusted, ps2Ajusted, pe2Ajusted};

        Arrays.sort(array, new Comparator<Vector2d>() {
            @Override
            public int compare(Vector2d o1, Vector2d o2) {
                double thetaDelta = o1.y - o2.y;

                if (thetaDelta < 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });


        if (array[0] == ps1Ajusted) {
            return array[2] == pe1Ajusted;
        }
        if (array[0] == pe1Ajusted) {
            return array[2] == ps1Ajusted;
        }
        if (array[0] == ps2Ajusted) {
            return array[2] == pe2Ajusted;
        }
        if (array[0] == pe2Ajusted) {
            return array[2] == ps2Ajusted;
        }

        throw new RuntimeException("Something's really broken");
    }


    private static Vector2d toPolar(Vector2d vec) {
        double r = Math.sqrt(vec.x * vec.x + vec.y * vec.y);
        double theta = Math.atan(vec.y / vec.x);

        return new Vector2d(r, theta);
    }
}
