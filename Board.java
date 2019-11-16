// Models a board in the 8-puzzle game or its generalization.
public class Board {
    private final int size; //size of the array
    private final int N; // dimension of the array 
    private int[][] tiles;
    private int hamming;
    private int manhattan;
    
    // Construct a board from an N-by-N array of tiles, where 
    // tiles[i][j] = tile at row i and column j, and 0 represents the blank 
    // square.
    public Board(int[][] tile) {
        /* tiles = new int [tile.length][tile.length];
        for (int i = 0; i < tile.length; i++)
        {
            for (int j = 0; j < tile[i].length; j++)
            {
                tiles[i][j] = tile[i][j];
            }
        }
        */
        tiles = tile;
        N = tile.length;
        size = N*N;
        hamming = hamming();
        manhattan = manhattan();
    }

    // Tile at row i and column j.
    public int tileAt(int i, int j) {
        return tiles[i][j];
    }
    
    // Size of this board.
    public int size() {
        return N;
    }

    // Number of tiles out of place.
    public int hamming() {
    int num = 0;
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[i].length; j++)
            {
                if (tiles[i][j] != (j+1)+i*N)
                {
                    num++;
                }
            }
        }
        
        return num;
    }

    // Sum of Manhattan distances between tiles and goal.
    public int manhattan() {
    int rowHolder;
    int colHolder;
    int s1 = 0;
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[i].length; j++)
            {
                if(tiles[i][j] % N == 0)
                {
                    rowHolder = N;
                    colHolder = tiles[i][j] / N;
                } else {
                    rowHolder = tiles[i][j] % N;
                    colHolder = tiles[i][j] / N +1;
                }
                
                s1 = s1 +  Math.abs(j - rowHolder) + Math.abs(i - colHolder);
            }
        }
        return s1;
    }

    // Is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j< tiles[i].length; j++)
            {
                if (tiles[i][j] == 0)
                {
                    continue;
                } else if (tiles[i][j] != (j+1)+i*N){
                    return false;
                }
            }
        }
        return true;
    }

    // Is this board solvable?
    public boolean isSolvable() {
    int blankSpot = 0;
        if (N%2 != 0)
        {
            if (inversions() %2 != 0)
            {
                return false;
            }
        } else if (N%2 == 0) {
           
            for (int i = 0; i < tiles.length; i++) 
            {
                 for (int j = 0; j < tiles[i].length; j++)
             {
                if (tiles[i][j] == 0)
                {
                    blankSpot = i;
                }
            }
        }
            if ((inversions() + blankSpot) % 2 == 0)
            {
                return false;
            }
        }
        return true;
    }

    // Does this board equal that?
    public boolean equals(Board that) {
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[i].length; j++)
            {
                if (tiles[i][j] != that.tiles[i][j])
                {
                    return false;
                }
            }
        }
        return true;
    }

    // All neighboring boards.
    public Iterable<Board> neighbors() {
        LinkedQueue<Board> q = new LinkedQueue<Board>();
        int row = 0;
        int col = 0;
        int[][] clone;
        Board a;
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[i].length; j++)
            {
                if (tiles[i][j] == 0)
                {
                    row = i;
                    col = j;
                }
            }
        }
        
          if ( row >= 1)
          {
            clone = cloneTiles();
            clone[row-1][col] = 0;
            clone[row][col] = tiles[row-1][col];
            a = new Board(clone);
            q.enqueue(a);
          }
          if (row <= N-2)
          {
            clone = cloneTiles();
            clone[row+1][col] = 0;
            clone[row][col] = tiles[row+1][col];
            a = new Board(clone);
            q.enqueue(a);
          }
          if (col >= 1)
          {
            clone = cloneTiles();
            clone[row][col-1] = 0;
            clone[row][col] = tiles[row][col-1];
            a = new Board(clone);
            q.enqueue(a);
          }
          if (col <= N-2)
          {
            clone = cloneTiles();
            clone[row][col+1] = 0;
            clone[row][col] = tiles[row][col+1];
            a = new Board(clone);
            q.enqueue(a);
          }
          
        
        return q;
    }
    

    // String representation of this board.
    public String toString() {
        String s = N + "\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s += String.format("%2d", tiles[i][j]);
                if (j < N - 1) {
                    s += " ";
                }
            }
            if (i < N - 1) {
                s += "\n";
            }
        }
        return s;
    }

    // Helper method that returns the position (in row-major order) of the 
    // blank (zero) tile.
    private int blankPos() {
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[i].length; j++)
            {
                if (tiles[i][j] == 0)
                {
                    return (i+1) * (j+1);
                }
            }
        }
        return -1;
    }

    // Helper method that returns the number of inversions.
    private int inversions() {
    int num = 0;
    int pos = 0;
    int[] arr = new int[size];
    for (int i = 0; i < tiles.length; i++)
    {
        for (int j = 0; j < tiles[i].length; j++)
        {
            arr[pos++] = tiles[i][j]; 
        }
    }
    
    for (int i = 0; i < arr.length; i++)
    {
        for (int j = i+1; j < arr.length; j++)
        {
            if (arr[i] > arr[j])
            {
                num++;
            }
        }
    }
        return num;
    }

    // Helper method that clones the tiles[][] array in this board and 
    // returns it.
    private int[][] cloneTiles() {
        int[][] clone = new int[N][N];
        for (int i = 0; i < clone.length; i++)
        {
            for (int j = 0; j < clone[i].length; j++)
            {
                clone[i][j] = tiles[i][j];
            }
        }
        
        return clone;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.isGoal());
        StdOut.println(board.isSolvable());
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
    }
}
