package theory;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author tadaki
 */
public class GiantCluster {

    private final double sAccuracy;
    private double s;

    /**
     * コンストラクタ
     *
     * @param sAccuracy 精度
     */
    public GiantCluster(double sAccuracy) {
        this.sAccuracy = sAccuracy;
        s = 0.1;
    }

    /**
     * 与えられたパラメタに対して、解を求める
     *
     * @param c
     * @return
     */
    public double find(double c) {
        if (c <= 1.) {
            return 0.;
        }
        s = updateS(c, s);
        return s;
    }

    /**
     * 近似を進める
     *
     * @param c
     * @param x
     * @return 
     */
    protected double updateS(double c, double x) {
        double sNew = func(c, x);
        while (Math.abs(sNew - x) > sAccuracy) {
            x = sNew;
            sNew = func(c, x);
        }
        return x;
    }

    private double func(double c, double x) {
        return 1. - Math.exp(-c * x);
    }

    public List<Point2D.Double> getOrbit(double c) {
        s = 0.1;
        List<Point2D.Double> list = Collections.synchronizedList(new ArrayList<>());
        list.add(new Point2D.Double(s, s));
        double sNew = func(c, s);
        while (Math.abs(sNew - s) > sAccuracy) {
            list.add(new Point2D.Double(s, sNew));
            list.add(new Point2D.Double(sNew, sNew));
            s = sNew;
            sNew = func(c, s);
        }
        return list;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        GiantCluster gc = new GiantCluster(1.e-10);
        double c = 1.;
        double dc = 1.e-3;
        try (PrintStream out
                = new PrintStream(new FileOutputStream("selfConsistent.txt"))) {
            while (c < 2.) {
                double x = gc.find(c);
                out.println(c + " " + x);
                c += dc;
            }
        }
        try (PrintStream out
                = new PrintStream(new FileOutputStream("selfConsistentOrbit.txt"))) {
            c = 1.5;
            List<Point2D.Double> list = gc.getOrbit(c);
            for (Point2D.Double p : list) {
                out.println(p.x + " " + p.y);
            }
        }
    }

}
