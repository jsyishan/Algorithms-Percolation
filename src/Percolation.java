
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by prinz_eugen on 3/26/17.
 */
public class Percolation {

    private WeightedQuickUnionUF uf;
    private int n;  //  width and length of the grid
    private int top, bottom;    //  the top's root and the bottom's root in the uf

    private int openSites;
    private int[][] grid;   //  n-by-n grid
    private boolean[][] ifOpenGrid; //  tells whether the grid[row][col] is open or not, true means open and false means blocked

    private int[][] nearby = {
            {0, 1},     //  right
            {0, -1},    //  left
            {1, 0},     //  down
            {-1, 0}     //  up
    };  //  to locate the nearby site of the current site

    /**
     * Initialize Percolation data type with {@code n} sites
     * Create a two-dimension array named {@code grid} and initialize its values all to blocked (the boolean false),
     * which means all sites blocked
     *
     * @param n the number of sites
     * @throws IllegalArgumentException if {@code n <= 0}
     */
    public Percolation(int n) {

        if (n <= 0) {
            throw new IllegalArgumentException("n should be bigger than 0!");
        }
        this.n = n;
        uf = new WeightedQuickUnionUF(n * n + 2);
        top = n * n;
        bottom = n * n + 1;

        openSites = 0;
        grid = new int[n + 1][n + 1];
        ifOpenGrid = new boolean[n + 1][n + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                grid[i][j] = n * (i - 1) + j - 1;
                ifOpenGrid[i][j] = false;
            }
        }

    }

    public void open(int row, int col) {

        int tx, ty; //  temp x and y (location)

        isOutOfBounds(row, col);
        if (ifOpenGrid[row][col]) {
            return;
        }
        openSites++;
        ifOpenGrid[row][col] = true;

        if (row == 1 && !uf.connected(grid[row][col], top)) {
            uf.union(grid[row][col], top);
        }

        if (row == n && !uf.connected(grid[row][col], bottom)) {
            uf.union(grid[row][col], bottom);
        }

        for (int i = 0; i < 4; i++) {
            tx = row + nearby[i][0];
            ty = col + nearby[i][1];
            if (tx < 1 || tx > n || ty < 1 || ty > n) {
                continue;
            }

            if (ifOpenGrid[tx][ty]) {
                uf.union(grid[row][col], grid[tx][ty]);
            }

        }
    }

    public boolean isOpen(int row, int col) {

        isOutOfBounds(row, col);
        return ifOpenGrid[row][col];
    }

    public boolean isFull(int row, int col) {

        isOutOfBounds(row, col);
        if (!isOpen(row, col)) {
            return false;
        }

        if (uf.connected(top, grid[row][col])) {
            return true;
        }
        return false;
    }


    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        if (uf.connected(top, bottom)) {
            return true;
        }
        return false;
    }


    private void isOutOfBounds(int row, int col) {

        if (row < 1 || col < 1 || row > n || col > n) {
            throw new IndexOutOfBoundsException("row and col should be within the range of 1 to n!");
        }

    }

    /**
     * just for test
     * @param args
     */
    public static void main(String[] args) {

        int n = StdIn.readInt();
        Percolation perc = new Percolation(n);

        while (!perc.percolates()) {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);
            if (!perc.isOpen(row, col)) {
                perc.open(row, col);
            }
        }

        double num = perc.numberOfOpenSites();
        StdOut.println("the estimate of percolation threshold is " + num / (n * n)); //  print the result
    }

}
