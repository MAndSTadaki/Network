package networkModels;

import java.io.IOException;
import java.util.List;
import network.*;

/**
 * Erdos-Renni random network
 *
 * @author tadaki
 */
public class ER extends AbstractNetwork {

    private final int N;
    private int L;

    public ER(int n, int m) {
        super("ER(" + String.valueOf(n) + "," + String.valueOf(m) + ")");
        this.N = n;
        this.L = m;
    }

    @Override
    public void createNetwork() {
        createNodes();
        createEdges();
    }

    private void createNodes() {
        for (int i = 0; i < N; i++) {
            addNode(new Node(String.valueOf(i))); }
    }

    private void createEdges() {
        List<Node> nodes = this.getNodeList();
        int i = 0;
        while (i < L) {
            int x = (int) (N * Math.random());
            int y = (int) (N * Math.random());
            Node v = nodes.get(x);
            Node w = nodes.get(y);
            if (!isConnected(v, w)) { connectNodes(v, w);i++;}
        }
    }

    public void addEdge(){
        int i = 0;
        while (i==0) {
            int x = (int) (N * Math.random());
            int y = (int) (N * Math.random());
            Node v = getNodeList().get(x);
            Node w = getNodeList().get(y);
            if (!isConnected(v, w)) { connectNodes(v, w);i++;L++;}
        }
    }
    
    public void addEdges(int nn){
        List<Node> nodes = this.getNodeList();
        int i = 0;
        while (i < nn) {
            int x = (int) (N * Math.random());
            int y = (int) (N * Math.random());
            Node v = nodes.get(x);
            Node w = nodes.get(y);
            if (!isConnected(v, w)) { connectNodes(v, w);i++;L++;}
        }
    }

    public int getL() {
        return L;
    }
    
    public static void main(String args[]) throws IOException {
        int N = 1000;
        int L = 24975;
        ER er = new ER(N, L);
        er.createNetwork();
        String filename = er.getLabel()+".net";
        Network2File.outputPajekData(filename, er);
    }
}
