/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private int numberOfMoves = -1;
    private Stack<Board> solutions = null;
    private boolean initialSolvable = false;


    private class SearchNode {
        private final Board board;
        private final int moves;
        private SearchNode prev;
        private int manhattanDistance;

        public SearchNode(Board b, int moves, SearchNode prev) {
            this.board = b;
            this.moves = moves;
            this.prev = prev;
            this.manhattanDistance = this.board.manhattan() + this.moves;
        }

        public final Comparator<SearchNode> orderByHamming() {
            return new Hamming();
        }

        private class Hamming implements Comparator<SearchNode> {
            public int compare(SearchNode node1, SearchNode node2) {
                return Integer.compare(
                        node1.board.hamming() + node1.moves, node2.board.hamming() + node2.moves
                );
            }
        }

        public final Comparator<SearchNode> orderByManhattan() {
            return new Manhattan();
        }

        private class Manhattan implements Comparator<SearchNode> {
            public int compare(SearchNode node1, SearchNode node2) {
                return Integer.compare(
                        node1.manhattanDistance,
                        node2.manhattanDistance
                );
            }
        }

    }

    private void addNeighborBoards(MinPQ<SearchNode> q,
                                   SearchNode node) {
        for (Board b : node.board.neighbors()) {
            if (node.prev == null)
                q.insert(new SearchNode(b, node.moves + 1, node));
            else if (!b.equals(node.prev.board)) {
                q.insert(new SearchNode(b, node.moves + 1, node));
            }
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        SearchNode root = new SearchNode(initial, 0, null);
        MinPQ<SearchNode> initialQueue = new MinPQ<SearchNode>(root.orderByManhattan());
        initialQueue.insert(root);

        SearchNode twinRoot = new SearchNode(initial.twin(), 0, null);
        MinPQ<SearchNode> twinQueue = new MinPQ<SearchNode>(twinRoot.orderByManhattan());
        twinQueue.insert(twinRoot);

        while (!initialQueue.isEmpty()) {
            SearchNode node = initialQueue.delMin();
            if (node.board.isGoal()) {
                initialSolvable = true;
                numberOfMoves = node.moves;

                // update solutions
                solutions = new Stack<>();
                SearchNode sol = new SearchNode(node.board, node.moves, node.prev);
                solutions.push(node.board);
                while (sol.prev != null) {
                    solutions.push(sol.prev.board);
                    sol = sol.prev;
                }
                break;
            }
            addNeighborBoards(initialQueue, node);

            // solvable check for twin board
            SearchNode twinNode = twinQueue.delMin();

            if (twinNode.board.isGoal()) {
                StdOut.println("Twin solvable!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                break;
            }
            addNeighborBoards(twinQueue, twinNode);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return initialSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return numberOfMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solutions;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
