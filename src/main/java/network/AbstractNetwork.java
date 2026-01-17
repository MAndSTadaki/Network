package network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author tadaki
 */
public abstract class AbstractNetwork {

    public final String label;
    public final SortedMap<Node, SortedSet<Edge>> node2Edge;
    private boolean directional = false;

    public AbstractNetwork(String label) {
        this.label = label;
        node2Edge = Collections.synchronizedSortedMap(new TreeMap<>()); }

    /**
     * Networkに頂点を追加
     *
     * @param node
     */
    public void addNode(Node node) {
        if (!node2Edge.keySet().contains(node)) {
            node2Edge.put(node, Collections.synchronizedSortedSet(new TreeSet<>()));
        } }

    /**
     * 二つの頂点を結ぶ辺を追加
     *
     * @param n1
     * @param n2
     * @return
     */
    public Edge connectNodes(Node n1, Node n2)
            throws IllegalArgumentException {
        if (node2Edge.keySet().contains(n1) && node2Edge.keySet().contains(n2)) {
            StringBuilder sb = new StringBuilder();
            sb.append("(").append(n1.label).append(",");
            sb.append(n2.label).append(")");
            Edge edge = new Edge(n1, n2, sb.toString());
            node2Edge.get(n1).add(edge);
            if (!directional) { node2Edge.get(n2).add(edge); }
            return edge;
        }
        String message = "node " + n1.label + " and " + n2.label
                + " can not be connected";
        throw new IllegalArgumentException(message);
    }

    /**
     * 実際にNetworkを構築する抽象メソッド
     *
     */
    abstract public void createNetwork();

    public SortedSet<Node> getNodes() {
        SortedSet<Node> nodes = Collections.synchronizedSortedSet(new TreeSet<>());
        node2Edge.keySet().forEach(n -> nodes.add(n));
        return nodes;
    }

    public List<Node> getNodeList() {
        SortedSet<Node> nodes = getNodes();
        List<Node> nodeList = Collections.synchronizedList(new ArrayList<>());
        nodes.stream().forEachOrdered(n -> nodeList.add(n));
        return nodeList;
    }

    public SortedSet<Edge> getEdges(Node n) { return node2Edge.get(n); }

    public boolean isDirectional() { return directional; }

    public void setDirectional(boolean directional) { this.directional = directional;  }

    public String getLabel() { return label; }

    public int getNumNode() { return node2Edge.keySet().size(); }

    /**
     * 二つの頂点を結ぶ辺の有無
     * @param from
     * @param to
     * @return 
     */
    public boolean isConnected(Node from,Node to){
        boolean b = false;
        if(node2Edge.keySet().contains(from)){
            Set<Edge> edges = getEdges(from);
            for(Edge e:edges){
                if(e.getTerminal(from).equals(to)){ b=true; } }
        }
        return b;
    }
}
