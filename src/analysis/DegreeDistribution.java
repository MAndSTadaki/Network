package analysis;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import network.*;
import networkModels.ER;

/**
 * 次数分布
 *
 * @author tadaki
 */
public class DegreeDistribution {

    private final AbstractNetwork network;//対象ネットワーク
    private final int bin;

    public DegreeDistribution(AbstractNetwork network, int bin) {
        this.network = network;
        this.bin = bin;
    }

    public DegreeDistribution(AbstractNetwork network) {
        this.network = network;
        this.bin = 1;
    }

    public List<Point2D.Double> evalDistribution() {
        List<Point2D.Double> points = Collections.synchronizedList(new ArrayList<>());
        List<Point2D.Double> hist = evalHistgram();
        double sum = 0;
        for (Point2D.Double p : hist) {
            sum += p.y;
        }
        for (Point2D.Double p : hist) {
            points.add(new Point2D.Double(
                    (double) p.x, (double) p.y / sum / bin));
        }
        return points;
    }

    public void printDistribution(String filename)
            throws IOException {
        try (PrintStream out 
                = new PrintStream(new FileOutputStream(filename)) ){
            List<Point2D.Double> points = evalDistribution();
            for (Point2D.Double p : points) {
                out.println(p.x+" "+p.y);
            }
        }
    }

    /**
     * ヒストグラム生成
     *
     * @return
     */
    public List<Point2D.Double> evalHistgram() {
        SortedMap<Integer, Integer> histogramMap
                = new TreeMap<>();
        for (Node node : network.getNodes()) {
            int degree = network.getEdges(node).size() / bin;
            int count = 1;
            if (histogramMap.containsKey(degree)) {
                count = histogramMap.get(degree);
                count++;
            }
            histogramMap.put(degree, count);
        }

        List<Point2D.Double> hist = Collections.synchronizedList(new ArrayList<>());
        for (Integer degree : histogramMap.keySet()) {
            int count = histogramMap.get(degree);
            hist.add(new Point2D.Double(
                    bin * degree , (double) count));
        }
        return hist;
    }

    /**
     * 平均次数
     *
     * @return
     */
    public double averageDegree() {
        double averageDegree = 0;
        SortedSet<Node> nodes = network.getNodes();
        for (Node node : nodes) {
            averageDegree += network.getEdges(node).size();
        }
        int n = network.getNumNode();
        return (double) averageDegree / n;
    }

    public static void main(String args[]) throws IOException {
        int N = 5000;
        double p = 0.05;
        int L = (int) (p * N * (N - 1) / 2);
        ER er = new ER(N, L);
        er.createNetwork();
        DegreeDistribution dist = new DegreeDistribution(er,5);
        dist.printDistribution(er.label + "-dist.txt");
    }
}
