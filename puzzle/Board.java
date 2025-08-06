/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public final class Board {
    private final int[][] board;
    private final int boardSize;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)

    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException();

        boardSize = tiles.length;
        board = new int[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            board[i] = Arrays.copyOf(tiles[i], boardSize);
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(boardSize).append("\n");
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++)
                sb.append(board[i][j]).append(" ");
            sb.append("\n");
        }

        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return boardSize;
    }

    private int correctValueAt(int row, int col) {
        if (row == boardSize - 1 && col == boardSize - 1) return 0;
        return row * boardSize + col + 1;
    }

    // number of tiles out of place
    public int hamming() {
        int distance = 0;
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (board[r][c] != 0 && board[r][c] != correctValueAt(r, c)) {
                    distance++;
                }
            }
        }

        return distance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int totalDistance = 0;
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                int value = board[r][c];
                if (value != 0) {
                    int trueRow = (value - 1) / boardSize;
                    int trueCol = (value - 1) % boardSize;

                    int distance = Math.abs(trueRow - r) + Math.abs(trueCol - c);
                    totalDistance += distance;
                }
            }
        }

        return totalDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || getClass() != y.getClass()) return false;

        Board b = (Board) y;
        if (b.dimension() != dimension()) return false;

        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (board[r][c] != b.board[r][c]) return false;
            }
        }
        return true;
    }

    private Board exchange(int row, int col, int newRow, int newCol) {
        int[][] copies = new int[boardSize][boardSize];

        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                copies[r][c] = board[r][c];
            }
        }

        int temp = copies[row][col];
        copies[row][col] = copies[newRow][newCol];
        copies[newRow][newCol] = temp;
        return new Board(copies);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> boards = new ArrayList<>();
        outer:
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (board[r][c] == 0) {
                    if (r > 0) boards.add(exchange(r, c, r - 1, c));
                    if (r < boardSize - 1) boards.add(exchange(r, c, r + 1, c));
                    if (c > 0) boards.add(exchange(r, c, r, c - 1));
                    if (c < boardSize - 1) boards.add(exchange(r, c, r, c + 1));
                    break outer;
                }
            }
        }

        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        ArrayList<int[]> positions = new ArrayList<>();
        int select = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] != 0) {
                    int[] pair = { i, j };
                    positions.add(pair);
                    select++;
                    if (select == 2) break;
                }
            }
        }

        return exchange(
                positions.get(0)[0],
                positions.get(0)[1],
                positions.get(1)[0],
                positions.get(1)[1]
        );
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        StdOut.println("Initial: ");
        StdOut.println(initial);

        StdOut.println("Neighbors: ");
        for (Board b : initial.neighbors()) {
            StdOut.println(b);
        }

        StdOut.println("Twin: ");
        StdOut.println(initial.twin());

        StdOut.println("Hamming: ");
        StdOut.println(initial.hamming());

        StdOut.println("Manhattan: ");
        StdOut.println(initial.manhattan());
    }

}
