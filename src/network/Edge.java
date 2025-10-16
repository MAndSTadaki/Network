package network;

/**
 *
 * @author tadaki
 */
public class Edge implements Comparable<Edge>{

    public final Node from;
    public final Node to;
    public final String label;
    public final double weight;

    public Edge(Node from, Node to, String label) {
        this.from = from;
        this.to = to;
        this.label = label;
        weight = 1.;
    }

    public Edge(Node from, Node to, String label, double weight) {
        this.from = from;
        this.to = to;
        this.label = label;
        this.weight = weight;
    }

    public Node getTerminal(Node n) throws IllegalArgumentException {
        if (n == from) {  return to;  }
        if (n == to) {  return from;  }
        String message = "no such node (" + n.label 
                + ")  to this edge (" + label + ")";
        throw new IllegalArgumentException(message);
    }

    @Override
    public int compareTo(Edge o) {  return label.compareTo(o.label);  }
    
}
