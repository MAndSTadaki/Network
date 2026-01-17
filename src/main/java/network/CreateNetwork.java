package network;

import java.io.IOException;

public class CreateNetwork {
    /**
     * テストネットワークの作成
     *
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        AbstractNetwork network = new AbstractNetwork("test") {
            @Override
            public void createNetwork() {
                int n = 4;
                Node nodes[] = new Node[n];
                for (int i = 0; i < n; i++) {
                    nodes[i] = new Node(String.valueOf(i));
                    addNode(nodes[i]);
                }
                connectNodes(nodes[0], nodes[1]);
                connectNodes(nodes[0], nodes[3]);
                connectNodes(nodes[1], nodes[2]);
                connectNodes(nodes[1], nodes[3]);
                connectNodes(nodes[2], nodes[3]);
            }
        };
        Network2File.outputPajekData("test.net", network);
    }    
}
