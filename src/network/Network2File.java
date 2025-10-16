package network;

import java.io.IOException;
import java.util.List;
import java.awt.geom.Point2D;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 *
 * @author tadaki
 */
public class Network2File {

    //改行文字（OSによる差を考慮）
    private static final String NL
            = System.getProperty("line.separator");
    private static final char QUOTE = 0x22;//ダブルクォーテーション

    public static enum ReadMode {
        ReadNode, ReadArcs, ReadEdges, None;
    }

    private Network2File() {
    }

    /**
     * pajek 用のデータを作成
     *
     * @param network
     * @return
     */
    public static String generatePajekData(AbstractNetwork network) {
        StringBuilder sb = new StringBuilder();
        List<Node> nodes = network.getNodeList();
        //頂点一覧を文字列化
        sb.append("*Vertices ").append(nodes.size()).append(NL);
        nodes.stream().forEach(
                node -> {
                    int nodeID = nodes.indexOf(node) + 1;
                    String nodeLabel = QUOTE + node.label + QUOTE;
                    sb.append(nodeID).append(" ").append(nodeLabel);
                    if (node.getP() != null) {
                        Point2D.Double p = node.getP();
                        sb.append(" ").append(p.x).append(" ");
                        sb.append(p.y).append(" ").append(node.getZ());
                    }
                    sb.append(NL);
                });
        //辺の情報の文字列化
        if (network.isDirectional()) {
            sb.append("*Arcs").append(NL);
            nodes.stream().forEach(
                    startNode -> {
                        //始点の頂点番号
                        int startNodeID = nodes.indexOf(startNode) + 1;
                        network.getEdges(startNode).stream().
                                map(edge -> edge.getTerminal(startNode)).
                                map(endNode -> nodes.indexOf(endNode) + 1).
                                forEach(endNodeID -> {//終点の頂点番号
                                    sb.append(startNodeID).append(" ");
                                    sb.append(endNodeID).append(" ").append(NL);
                                });
                    });
        } else {
            sb.append("*Edges").append(NL);
            nodes.stream().forEach(
                    startNode -> {
                        int startNodeID = nodes.indexOf(startNode) + 1;
                        network.getEdges(startNode).stream().
                                map(edge -> edge.getTerminal(startNode)).
                                map(endNode -> nodes.indexOf(endNode) + 1).
                                filter(endNodeID -> (endNodeID > startNodeID)).
                                forEach(endNodeID -> {
                                    sb.append(startNodeID).append(" ");
                                    sb.append(endNodeID).append(" ").append(NL);
                                });
                    });
        }
        return sb.toString();
    }

    /**
     * pajek 用のデータを出力
     *
     * @param filename
     * @param network
     * @throws java.io.IOException
     */
    public static void outputPajekData(
            String filename, AbstractNetwork network)
            throws IOException {
        String str = generatePajekData(network);
        try (PrintStream out = new PrintStream(new FileOutputStream(filename))) {
            out.println(str);
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        AbstractNetwork network = File2Network.createNetworkFromFile("test.net", "test");
        outputPajekData("out.net", network);
    }

}
