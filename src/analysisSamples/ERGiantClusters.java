package analysisSamples;

import analysis.GiantComponents;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import networkModels.ER;

/**
 *
 * @author tadaki
 */
public class ERGiantClusters {

    public static void main(String args[]) throws FileNotFoundException {
        int N = 10000;
        int l = 0;
        ER er = new ER(N, 0);
        er.createNetwork();
        String filename = ERGiantClusters.class.getSimpleName() + "-" + String.valueOf(N) + ".txt";
        try (PrintStream out = new PrintStream(
                new FileOutputStream(new File(filename)))) {
            for (int i = 1; i < 100; i++) {
                double pp = 0.03 * i;
                double p = pp / N;
                int numEdges = (int) (p * N * (N - 1) / 2);
                int newL = numEdges - l;
                er.addEdges(newL);
                GiantComponents gc = new GiantComponents(er);
                gc.findClusters();
                int k = gc.findLargestClusterSize();
                out.println(pp + " " + k);
                l = numEdges;
            }
        }
    }
}
