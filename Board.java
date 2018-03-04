import edu.princeton.cs.algs4.Queue;

public class Board
{
    private int[][] board;
    private final int N;
    private int hamming = -1;
    private int manhattan = -1;
    
    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
    {
        N = blocks.length;
        board = copy(blocks);
    }
    
    private int[][] copy(int[][] blocks)
    {
        int[][] copy = new int[N][N];
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++) copy[i][j] = blocks[i][j];
        }
        return copy;
    }
        
                                           // (where blocks[i][j] = block in row i, column j)
    public int dimension()                 // board dimension n
    { return N; }
        
    public int hamming()                   // number of blocks out of place
    { 
        if (hamming == -1) { // avoid always calculate hamming from scratch...
           int cnt = 0;
            for (int i = 0; i < N; i++)
            {
                for (int j = 0; j < N; j++) 
                {
                    int value = board[i][j];
                    if (value != 0 && value != N*i + j + 1) cnt++;
                }
            }
            hamming = cnt;
        }
        return hamming; 
    }
    
    private int hammingstep(int i, int j)
    { 
        int value = board[i][j];
        if (value != 0 && value != N*i + j + 1)
            return 1;
        else return 0;
    } 
    
    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        if (manhattan == -1){
            int cnt = 0;
            for (int i = 0; i < N; i++)
            {
                for (int j = 0; j < N; j++) 
                {
                    int value = board[i][j];
                    if (value != 0 && value != N*i + j + 1) cnt += Math.abs((value-1)/N - i) + Math.abs((value-1)%N - j);
                }
            }
            manhattan = cnt;
        }
        return manhattan;
    }
    
    private int manhattanstep(int i, int j)
    { 
        int value = board[i][j];
        if (value != 0 && value != N*i + j + 1)
            return Math.abs((value-1)/N - i) + Math.abs((value-1)%N - j); 
        else return 0;
    }
    
    public boolean isGoal()                // is this board the goal board?
    { return hamming() == 0; }
    
    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        if (board[0][0] != 0 && board[0][1] != 0)
            return new Board(swap(0,0,0,1));
        else return  new Board(swap(1,0,1,1));
    }
    
    private int[][] swap(int i, int j, int m, int n)
    {
        int[][] copy = copy(board);
        int t = copy[i][j];
        copy[i][j] = copy[m][n];
        copy[m][n] = t;
        
        return copy;
    }
    
    public boolean equals(Object y)        // does this board equal y?
    {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (that.N != this.N) return false;
        else
        {
            for (int i = 0; i < N; i++){
                for (int j = 0; j < N; j++){
                if (board[i][j] != that.board[i][j])
                    return false;}
            }
        }
        return true;
    }
    
    public Iterable<Board> neighbors()     // all neighboring boards
    {
        int i = 0;
        int j = 0;
        for (int m = 0; m < N; m++){
            for (int n = 0; n < N; n++){
                if (board[m][n] == 0) 
                { 
                    i = m;
                    j = n;
                    break;
                }
            }
        }
        Queue<Board> neighbors = new Queue<Board>();
        if (i-1 >= 0) neighbors.enqueue(neighbor(i,j,i-1,j));
        if (i+1 < N) neighbors.enqueue(neighbor(i,j,i+1,j));
        if (j-1 >= 0) neighbors.enqueue(neighbor(i,j,i,j-1));
        if (j+1 < N) neighbors.enqueue(neighbor(i,j,i,j+1));
        return neighbors;
    }
    
    private Board neighbor(int row1, int col1, int row2, int col2) //change blank space from row1, col1 to row2, col2
    { 
        Board neighbor = new Board(swap(row1, col1, row2, col2));
        neighbor.hamming = this.hamming() + neighbor.hammingstep(row1, col1) - this.hammingstep(row2, col2); 
        neighbor.manhattan = this.manhattan() + neighbor.manhattanstep(row1, col1) - this.manhattanstep(row2, col2);
        return neighbor;
    }
    
                 
    public String toString()               // string representation of this board (in the output format specified below)
    {
        int N = board.length;
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString(); 
    }
}