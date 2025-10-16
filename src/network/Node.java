package network;

import java.awt.geom.Point2D;

/**
 *
 * @author tadaki
 */
public class Node implements Comparable<Node> {

    public final String label;
    //座標（必要に応じて利用）
    private Point2D.Double p;
    private double z;

    public Node(String label) {  this.label = label;  }

    @Override
    public int compareTo(Node o) {  return label.compareTo(o.label); }

    public double getZ() {  return z;  }

    public void setZ(double z) {  this.z = z;  }

    public Point2D.Double getP() {  return p;  }

    public void setP(Point2D.Double p) {  this.p = p;  }

}
