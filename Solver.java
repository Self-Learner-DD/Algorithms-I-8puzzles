import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver 
{
    private int step;
    private boolean solvable;
    private Stack<Board> solution = new Stack<Board>();
        
    private class Node implements Comparable<Node>// the Key of PQ need to be comparable
    {
        Board board;
        int moves;
        Node predecessor;
        int manh;
        
        Node(Board current, int move, Node prev)
        { board = current; moves = move; predecessor = prev; manh = current.manhattan();} // avoid repeating call of manhattan();
        
        private int cost1()
        { return moves + manh; }
        
        private int cost2()
        { return manh; }
                   
        public int compareTo(Node that)
        { 
            int diff = this.cost1() - that.cost1();
            if (diff == 0) return this.cost2() - that.cost2();
            else return diff;
        }
    }
        
    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        assertBoard(initial);
        Board twin = initial.twin();
        
        MinPQ<Node> pq = new MinPQ<Node>();  // use one PQ instead...
        //MinPQ<Node> pq_twin = new MinPQ<Node>();
        
        pq.insert(new Node(initial, 0, null));
        pq.insert(new Node (twin, 0, null));
        
        Node current = pq.delMin();
        while (!current.board.isGoal())
        {
                
            for (Board neighbor : current.board.neighbors())
            {
                if (current.predecessor == null) pq.insert(new Node(neighbor, current.moves+1, current));
                else if (!neighbor.equals(current.predecessor.board)) pq.insert(new Node(neighbor, current.moves+1, current)); // must use equals not ==, otherwise timing will be way too bad...
            }
          
            current = pq.delMin();
        }
        if (current.board.isGoal())
        {
            step = current.moves;
            while (current != null)
            {
                solution.push(current.board);
                current = current.predecessor;
            }
            if (solution.pop().equals(initial)) solvable = true;
            solution.push(initial);
        }
            
    }
        
    public boolean isSolvable()            // is the initial board solvable?
    { return solvable; }
    
    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    { 
        if (isSolvable()) return step;
        else return -1;
    }
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        if (isSolvable()) return solution;
        else return null;
    }
    
    private void assertBoard(Board board)
    {
        if (board == null) throw new IllegalArgumentException("Argument cannot be null");
    }
    
    public static void main(String[] args) 
    {      
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

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