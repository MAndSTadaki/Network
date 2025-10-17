package analysis;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import network.*;

/**
 *
 * @author tadaki
 */
public class DistanceDistribution {

    private final AbstractNetwork network;//対象ネットワーク

    private Map<String, Integer> distance;

    public DistanceDistribution(AbstractNetwork network) {
        this.network = network;
    }

    /**
     * 最短距離分布を
     * @return 
     */
    public List<Point2D.Double> getDistribution() {
        List<Point2D.Double> plist = Collections.synchronizedList(new ArrayList<>());
        Map<Integer, Integer> map = new TreeMap<>();
        List<Node> nodeList = network.getNodeList();
        int n = 0;
        for (Node v : nodeList) {
            for (Node w : nodeList) {
                String k = v.label + "-" + w.label;
                if (!v.equals(w)) {
                    n++;
                    int d = distance.get(k);
                    if (map.containsKey(d)) {
                        int c = map.get(d);
                        c++;
                        map.put(d, c);
                    } else {
                        map.put(d, 1);
                    }
                }
            }
        }
        for (Integer k : map.keySet()) {
            double d = 1. * map.get(k) / n;
            plist.add(new Point2D.Double(k, d));
        }
        return plist;
    }

    /**
     * Floyd Warshall method
     * 頂点間の
     */
    private void floydMethod() {
        distance = Collections.synchronizedMap(new HashMap<>());
        List<Node> nodeList = network.getNodeList();
        int num_edges = 0;
        for (Node v : nodeList) {
            num_edges += network.getEdges(v).size();
        }
        num_edges /= 2;
        for (Node v : nodeList) {
            for (Node w : nodeList) {
                String nodePair = v.label + "-" + w.label;
                if (v.equals(w)) {
                    distance.put(nodePair, 0);
                } else {
                    if (network.isConnected(v, w)) {
                        distance.put(nodePair, 1);
                    } else {
                        distance.put(nodePair, 10 * num_edges);
                    }
                }
            }
        }

        for (Node k : nodeList) {
            for (Node v : nodeList) {
                for (Node w : nodeList) {
                    int len_between = distance.get(v.label + "-" + k.label)
                            + distance.get(k.label + "-" + w.label);
                    int len_direct = distance.get(v.label + "-" + w.label);
                    if (len_between < len_direct) {
                        distance.put(v.label + "-" + w.label, len_between);
                    }
                }
            }
        }
    }

    public Map<String, Integer> getDistance() {
        if (distance == null) {
            floydMethod();
        }
        return distance;
    }

    public static void main(String args[]) throws IOException {

        AbstractNetwork network = new AbstractNetwork("sample") {
            @Override
            public void createNetwork() {
                for (int i = 0; i < 5; i++) {
                    addNode(new Node(String.valueOf(i)));
                }
                List<Node> nodes = getNodeList();
                connectNodes(nodes.get(0), nodes.get(1));
                connectNodes(nodes.get(0), nodes.get(2));
                connectNodes(nodes.get(1), nodes.get(3));
                connectNodes(nodes.get(2), nodes.get(3));
                connectNodes(nodes.get(3), nodes.get(4));

            }
        };
        network.createNetwork();
        DistanceDistribution dist = new DistanceDistribution(network);
        Map<String, Integer> map = dist.getDistance();
        for (String s : map.keySet()) {
            System.out.println(s + ":" + map.get(s));
        }
        List<Point2D.Double> plist = dist.getDistribution();
        for(Point2D.Double p:plist){
            System.out.println(p);
        }
    }
}
