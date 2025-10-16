package networkModels;

import analysis.DegreeDistribution;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import network.*;

/**
 * Barabasi-Albert
 *
 * @author tadaki
 */
public class BA extends AbstractNetwork {

    private final int n;//頂点の数
    private final int m;//新規に生成する頂点から出る辺の数
    //このリスト中に頂点が次数回登場する
    //つまり、ある頂点vの次数がkならば、vがk回現れる
    private List<Node> selectTarget;

    /**
     * コンストラクタ
     *
     * @param n
     * @param m
     */
    public BA(int n, int m) {
        super("BA(" + String.valueOf(n) + "," + String.valueOf(m) + ")");
        this.n = n;
        this.m = m;
        if (n < m) {
            throw new UnsupportedOperationException(
                    "Can not create such network.");
        }
        selectTarget = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public void createNetwork() {
        initialize();
        for (int i = m + 1; i < n; i++) {
            addNewNode();
        }
    }

    /**
     * 一つの頂点を追加する
     */
    private void addNewNode() {
        Node node = new Node(String.valueOf(getNumNode() + 1));
        addNode(node);
        List<Node> target = Collections.synchronizedList(new ArrayList<>());
        int nn = selectTarget.size();
        //接続先頂点を選び、targetへ追加
        for (int i = 0; i < m; i++) {
            int k = (int) (nn * Math.random());
            target.add(selectTarget.get(k));
        }
        //辺の生成
        for (Node t : target) {
            connectNodes(node, t);
            selectTarget.add(t);
            selectTarget.add(node);
        }
    }

    /**
     * 初期化
     *
     */
    private void initialize() {
        //m個の頂点を生成
        for (int i = 0; i <= m; i++) {
            addNode(new Node(String.valueOf(i)));
        }
        List<Node> nodes = getNodeList();
        //頂点相互を二重に結ぶ
        for (int i = 0; i <= m; i++) {
            Node x = nodes.get(i);
            for (int j = 0; j <= m; j++) {
                Node y = nodes.get(j);
                connectNodes(x, y);
                selectTarget.add(x);
                selectTarget.add(y);
            }
        }
    }

    public static void main(String args[]) throws IOException {
        boolean isDist=true;
        int n = 100;
        int m = 1;
        BA ba = new BA(n, m);
        ba.createNetwork();
        Network2File.outputPajekData(ba.getLabel()+"net", ba);
        
        if(isDist){
            n=1000000;
            ba = new BA(n,m);
            ba.createNetwork();
            DegreeDistribution dist = new DegreeDistribution(ba);
            dist.printDistribution(ba.label + "-dist.txt");
        }
    }

}
