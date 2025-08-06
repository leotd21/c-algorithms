/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */


import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF quf;
    private WeightedQuickUnionUF checkQUF;
    private int n;
    private int openedSites = 0;
    private int topSite;
    private int bottomSite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.grid = new boolean[n + 1][n + 1];
        this.quf = new WeightedQuickUnionUF(n * n + 2);
        this.checkQUF = new WeightedQuickUnionUF(n * n + 1);
        this.n = n;
        this.topSite = 0;
        this.bottomSite = n * n + 1;
    }

    private boolean checkInput(int row, int col) {
        if (row < 1 || row > this.n || col < 1 || col > this.n) {
            return false;
        }
        return true;
    }

    private int convertUnion(int r, int c) {
        return (r - 1) * this.n + c;
    }

    private void littleUnion(int row, int col, int r, int c) {
        if (checkInput(r, c) && isOpen(r, c)) {
            this.quf.union(convertUnion(row, col), convertUnion(r, c));
            this.checkQUF.union(convertUnion(row, col), convertUnion(r, c));
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!checkInput(row, col)) throw new IllegalArgumentException();
        if (isOpen(row, col)) return;

        this.grid[row][col] = true;
        this.openedSites += 1;
        if (row == 1) {
            this.quf.union(topSite, convertUnion(row, col));
            this.checkQUF.union(topSite, convertUnion(row, col));
        }
        if (row == this.n) {
            this.quf.union(bottomSite, convertUnion(row, col));
        }

        littleUnion(row, col, row - 1, col);
        littleUnion(row, col, row + 1, col);
        littleUnion(row, col, row, col + 1);
        littleUnion(row, col, row, col - 1);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!checkInput(row, col)) throw new IllegalArgumentException();
        return this.grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!checkInput(row, col)) throw new IllegalArgumentException();
        int cell = convertUnion(row, col);
        return isOpen(row, col) && (this.checkQUF.find(cell) == this.checkQUF.find(this.topSite));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.openedSites;
    }

    // does the system percolate
    public boolean percolates() {
        return this.quf.find(topSite) == this.quf.find(bottomSite);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(2);
        StdOut.println(p.isFull(1, 1));
        StdOut.println(p.isFull(2, 1));
        p.open(1, 1);
        p.open(2, 1);
        StdOut.println(p.isFull(1, 1));
        StdOut.println("Percolation:");
        StdOut.println(p.percolates());
        StdOut.println(p.numberOfOpenSites());
    }
}
