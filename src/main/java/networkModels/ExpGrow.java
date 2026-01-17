package networkModels;

import analysis.DegreeDistribution;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import network.*;

/**
 * Exponential-Grow network
 *
 * @author tadaki
 */
public class ExpGrow extends AbstractNetwork {

    private final int n;//頂点の数
    private final int m;//新規に生成する頂点から出る辺の数

    /**
     * コンストラクタ
     *
     * @param n
     * @param m
     */
    public ExpGrow(int n, int m) {
        super("EG(" + String.valueOf(n) + "," + String.valueOf(m) + ")");
        this.n = n;
        this.m = m;
        if (n < m) {
            throw new UnsupportedOperationException(
                    "Can not create such network.");
        }
    }

    @Override
    public void createNetwork() {
        initialize();
        for (int t = m + 2; t <= n; t++) {
            addNewNode();
        }
    }

    /**
     * 新たに頂点をネットワークに追加する
     *
     */
    private void addNewNode() {
        Node node = new Node(String.valueOf(getNumNode()));//新たな頂点
        List<Node> target = Collections.synchronizedList(new ArrayList<>());//接続先頂点リスト
        List<Node> nodes = getNodeList();
        int nn = nodes.size();
        for (int i = 0; i < m; i++) {//接続先をm 個ランダムに選ぶ
            int k = (int) (nn * Math.random());
            target.add(nodes.get(k));
        }
        //新頂点をネットワークに追加
        addNode(node);
        for (Node t : target) {//新頂点と既存頂点の接続
            connectNodes(node, t);
        }
    }

    /**
     * 初期配置の生成
     *
     * @return
     */
    private void initialize() {
        for (int i = 0; i <= m; i++) {//m個の頂点を生成
            Node node = new Node(String.valueOf(i));
            addNode(node);
        }
        List<Node> nodes = getNodeList();
        //それぞれを二重に結ぶ
        for (int i = 0; i <= m; i++) {
            Node x = nodes.get(i);
            for (int j = 0; j <= m; j++) {
                Node y = nodes.get(j);
                connectNodes(x, y);
            }
        }
    }

    /**
     * サンプルを生成
     *
     * @param args
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        boolean isDist = false;
        boolean largeGraph = true;
        int n = 100;
        int m = 1;
        ExpGrow eg = new ExpGrow(n, m);
        eg.createNetwork();
        String filename = eg.label + ".net";
        Network2File.outputPajekData(filename, eg);

        if (largeGraph) {
            n = 1000;
            eg = new ExpGrow(n, m);
            eg.createNetwork();
            filename = eg.label + ".net";
            Network2File.outputPajekData(filename, eg);
        }
        if (isDist) {
            n = 10000;
            eg = new ExpGrow(n, m);
            eg.createNetwork();
            DegreeDistribution dist = new DegreeDistribution(eg);
            dist.printDistribution(eg.label + "-dist.txt");
        }
    }

}
