package network;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tadaki
 */
public class File2Network {

    private File2Network() {
    }

    /**
     * pajekファイルからネットワークを構成
     *
     * @param filename
     * @param title
     * @return
     * @throws IOException
     */
    public static AbstractNetwork createNetworkFromFile(
            String filename, String title) throws IOException {
        //ネットワーククラスのインスタンスを生成
        AbstractNetwork network = new AbstractNetwork(title) {
            @Override
            public void createNetwork() {
            }
        };
        try (BufferedReader reader = openReader(filename)) {
            readFile(reader, network);
        }
        return network;
    }

    private static void readFile(BufferedReader reader, AbstractNetwork network)
            throws IOException {
        Network2File.ReadMode mode = Network2File.ReadMode.None;
        String line;
        int numNode = 0;
        //頂点の情報の読み始め
        while ((line = reader.readLine()) != null) {
            Pattern p = Pattern.compile("^\\*Vertices\\s+(\\d+)");
            Matcher m = p.matcher(line);
            if (m.find()) {
                numNode = Integer.valueOf(m.group(1));
                mode = Network2File.ReadMode.ReadNode;
                break;
            }
        }
        if (mode != Network2File.ReadMode.ReadNode) {
            return;
        }

        Node nodeArray[] = new Node[numNode];
        while ((line = reader.readLine()) != null) {
            if (line.contains("*Arcs")) {//辺の情報が始まったら次へ
                mode = Network2File.ReadMode.ReadArcs;
                network.setDirectional(true);
                break;
            }
            if (line.contains("*Edges")) {//辺の情報が始まったら次へ
                mode = Network2File.ReadMode.ReadEdges;
                network.setDirectional(false);
                break;
            }
            //実際に頂点の情報を読み込む
            readNodeFromString(line, nodeArray);
        }
        for (Node nn : nodeArray) {
            network.addNode(nn);
        }

        if (mode != Network2File.ReadMode.ReadArcs
                && mode != Network2File.ReadMode.ReadEdges) {
            return;
        }

        while ((line = reader.readLine()) != null) {
            connectNodes(line, network);
        }
    }

    /**
     * 実際に辺の情報を読み込む
     *
     * @param line
     * @param network
     */
    static private void connectNodes(String line, AbstractNetwork network) {
        String str[] = line.split("\\s");
        int i = Integer.valueOf(str[0]);
        int j = Integer.valueOf(str[1]);
        Node f = network.getNodeList().get(i - 1);
        Node t = network.getNodeList().get(j - 1);
        network.connectNodes(f, t);
    }

    /**
     * 実際に頂点の情報を読み込む
     *
     * @param line
     * @param nodeArray
     */
    static private void readNodeFromString(String line, Node nodeArray[]) {
        Pattern p = Pattern.compile("^(\\d+)\\s+\"(\\S+)\""
                + "(\\s+(\\d+\\.\\d+)\\s+(\\d+\\.\\d+)"
                + "\\s+(\\d+\\.\\d+))?");
        Matcher m = p.matcher(line);
        if (m.find()) {
            String label = m.group(2);
            int index = Integer.valueOf(m.group(1)) - 1;
            Node node = new Node(label);
            if (m.group(3) != null) {
                double x = Double.valueOf(m.group(4));
                double y = Double.valueOf(m.group(5));
                double z = Double.valueOf(m.group(6));
                node.setP(new Point2D.Double(x, y));
                node.setZ(z);
            }
            nodeArray[index] = node;
        }
    }

    /**
     * ファイル名を指定してBufferedReaderを開く
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static BufferedReader openReader(String filename) throws
            IOException {
        FileInputStream fStream = new FileInputStream(new File(filename));
        return new BufferedReader(new InputStreamReader(fStream));
    }

}
