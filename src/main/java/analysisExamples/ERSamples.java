package analysisExamples;

import java.io.IOException;
import network.Network2File;
import networkModels.ER;

/**
 *
 * @author tadaki
 */
public class ERSamples {

    /**
     * サンプルを生成
     *
     * @param args
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        int n = 100;
        Double pArray[]={0.005,0.008,0.009,0.01,0.011,0.012,0.02,0.03};
        int l = 0;
        ER er = new ER(n, 0);
        er.createNetwork();
        for (Double p:pArray) {
            int numEdges = (int) (p * n * (n - 1) / 2);
            int newL = numEdges - l;
            er.addEdges(newL);
            String filename="ER-"+n+"-"+p+".net";
            Network2File.outputPajekData(filename,er);
            l = numEdges;
        }
    }

}
