package analysis;

import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import network.AbstractNetwork;
import network.Edge;
import network.Node;

/**
 *
 * @author tadaki
 */
public class Search {
    private Search(){}
    
    /**
     * 幅優先探索
     *
     * @param bNetwork 対象となるグラフ
     * @param start 開始Node
     * @return 開始ノードから到達できる頂点の集合
     */
    public static Set<Node> BFS(AbstractNetwork bNetwork, Node start) {
        Set<Node> checked = Collections.synchronizedSet(new HashSet<>());
        Queue<Node> queue = new ConcurrentLinkedQueue<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            Node v = queue.poll();
            SortedSet<Edge> edges = bNetwork.getEdges(v);
            if (!edges.isEmpty()) {
                edges.stream().map(//v に接する全ての辺
                        e -> e.getTerminal(v)//v と反対側の頂点wを得る
                ).filter(//wがchecked にもqueueにも含まれていない
                        w -> (!checked.contains(w) && !queue.contains(w))
                ).forEach(//wをqueueへ加える
                        w -> queue.add(w));
            }
            checked.add(v);
        }
        return checked;
    }

}
