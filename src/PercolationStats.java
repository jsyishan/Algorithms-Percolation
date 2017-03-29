import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;
/**
 * Created by prinz_eugen on 3/26/17.
 */
public class PercolationStats {

    private int n;
    private int t;   //  trails
    private double[] results;   //  tresholds

    public PercolationStats(int n, int trails) {

        if (n <= 0 || trails <= 0) {
            throw new IllegalArgumentException("n and trails should be bigger than 0!");
        }

        this.n = n;
        t = trails;
        results = new double[trails];

        for (int i = 0; i < trails; i++) {
            StdRandom.setSeed(i);
            results[i] = calcPerc();
        }

    }

    private double calcPerc() {
        Percolation perc = new Percolation(n);

        while (!perc.percolates()) {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);
            if (!perc.isOpen(row, col)) {
                perc.open(row, col);
            }
        }

        double num = perc.numberOfOpenSites();
        return num / (n * n);

    }

    public double mean() {
        return StdStats.mean(results);
    }

    public double stddev() {
        return StdStats.stddev(results);
    }

    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(t);
    }

    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(t);
    }

    public static void main(String[] args) {
        int in = StdIn.readInt();
        int it = StdIn.readInt();

        PercolationStats percStats = new PercolationStats(in, it);
        StdOut.println("mean\t\t\t\t\t= " + percStats.mean());
        StdOut.println("stddev\t\t\t\t\t= " + percStats.stddev());
        StdOut.println("95% confidence interval = [" +
                percStats.confidenceLo() + ", " + percStats.confidenceHi() + "]");
    }

}
