package analysis;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Queue;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import network.*;

/**
 *
 * @author tadaki
 */
public class GiantComponents {

    private final AbstractNetwork network;
    private List<Set<Node>> clusters;

    /**
     * 改行コード
     */
    public static final String NL = System.getProperty("line.separator");

    public GiantComponents(AbstractNetwork network) {
        this.network = network;
    }

    /**
     * clusterへの分割
     *
     * @return cluster数
     */
    public int findClusters() {
        clusters = Collections.synchronizedList(new ArrayList<>());
        List<Node> nodes = Collections.synchronizedList(new ArrayList<>());
        network.getNodes().stream().forEach(
                node -> nodes.add(node)
        );
        while (!nodes.isEmpty()) {
            Node start = nodes.get(0);
            Set<Node> checked = Search.BFS(network, start);
            checked.stream().forEach(
                    n -> nodes.remove(n)
            );
            clusters.add(checked);
        }
        return clusters.size();
    }

    /**
     * 最大のclusterを求める
     *
     * @return
     */
    public Set<Node> findLargestCluster() {
        Set<Node> largest = Collections.synchronizedSet(new HashSet<>());
        for (Set<Node> set : clusters) {
            if (set.size() > largest.size()) {
                largest = set;
            }
        }
        return largest;
    }

    /**
     * 最大のclusterを大きさを求める
     *
     * @return
     */
    public int findLargestClusterSize() {
        return findLargestCluster().size();
    }

    /**
     * クラスタの情報をファイルへ出力
     *
     * @param clusters
     * @param filename
     * @throws IOException
     */
    public static void showClusters(List<Set<Node>> clusters, String filename)
            throws IOException {
        try (PrintStream out 
                = new PrintStream(new FileOutputStream(filename)) ) {
            String str = clusters2String(clusters);
            out.println(str);
        }
    }

    /**
     * クラスタの情報を文字列へ変換
     *
     * @param clusters
     * @return
     */
    public static String clusters2String(List<Set<Node>> clusters) {
        StringBuilder sb = new StringBuilder();
        for (Set<Node> set : clusters) {
            sb.append("{");
            set.stream().forEach(
                    n -> sb.append(n.toString()).append(",")
            );
            int last = sb.length() - 1;
            sb.deleteCharAt(last);
            sb.append("}");
            sb.append(NL);
        }
        return sb.toString();
    }

    /**
     * clusterを返す
     *
     * @return
     */
    public List<Set<Node>> getClusters() {
        return clusters;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        ///テスト用のネットワークを定義
        AbstractNetwork network = new AbstractNetwork("network") {
            @Override
            public void createNetwork() {
                for (int i = 0; i < 10; i++) {
                    Node n = new Node(String.valueOf(i));
                    this.addNode(n);
                }
                List<Node> nodes = this.getNodeList();
                connectNodes(nodes.get(0), nodes.get(1));
                connectNodes(nodes.get(0), nodes.get(2));
                connectNodes(nodes.get(0), nodes.get(3));
                connectNodes(nodes.get(1), nodes.get(4));
                connectNodes(nodes.get(5), nodes.get(6));
                connectNodes(nodes.get(6), nodes.get(7));
            }
        };
        network.createNetwork();
        GiantComponents gc = new GiantComponents(network);
        int k = gc.findClusters();
        System.out.println(k);
        GiantComponents.showClusters(gc.getClusters(), "GiantCluster.txt");
    }

}
