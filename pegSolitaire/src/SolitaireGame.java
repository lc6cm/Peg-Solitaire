
public abstract class SolitaireGame {

    protected int[][] board;
    protected int size;
    protected String boardType;

    public SolitaireGame(int size, String boardType) {
        this.size = size;
        this.boardType = boardType;
        this.board = new int[size][size];
        initBoard();
    }

    // Subclasses define how the board is set up
    protected abstract void initBoard();

    // Subclasses define how a move is made
    public abstract boolean makeMove(int fromRow, int fromCol, int toRow, int toCol);

    // Subclasses define how to check if game is over
    public abstract boolean isGameOver();

    // Subclasses define how to get a status message
    public abstract String getStatusMessage();

    // Randomizes the board state (shared by both modes)
    public void randomizeBoard() {
        java.util.Random rand = new java.util.Random();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board[r][c] != 0) {
                    board[r][c] = rand.nextBoolean() ? 1 : 2;
                }
            }
        }
    }

    // Returns true if a move from->to is valid (jumps over a peg into empty hole)
    protected boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (board[fromRow][fromCol] != 1) return false;
        if (board[toRow][toCol] != 2) return false;

        int dRow = toRow - fromRow;
        int dCol = toCol - fromCol;

        // Must move exactly 2 cells in one direction
        if (!((Math.abs(dRow) == 2 && dCol == 0) || (Math.abs(dCol) == 2 && dRow == 0))) {
            return false;
        }

        int midRow = fromRow + dRow / 2;
        int midCol = fromCol + dCol / 2;

        // Middle cell must have a peg to jump over
        return board[midRow][midCol] == 1;
    }

    // Executes a validated move on the board
    protected void executeMove(int fromRow, int fromCol, int toRow, int toCol) {
        int midRow = fromRow + (toRow - fromRow) / 2;
        int midCol = fromCol + (toCol - fromCol) / 2;
        board[fromRow][fromCol] = 2; // from becomes empty
        board[midRow][midCol] = 2;   // jumped peg removed
        board[toRow][toCol] = 1;     // destination gets peg
    }

    // Counts remaining pegs
    public int getPegCount() {
        int count = 0;
        for (int[] row : board)
            for (int cell : row)
                if (cell == 1) count++;
        return count;
    }

    // Checks if any valid move exists anywhere on the board
    public boolean hasAnyValidMove() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board[r][c] == 1) {
                    int[][] dirs = {{-2,0},{2,0},{0,-2},{0,2}};
                    for (int[] d : dirs) {
                        int nr = r + d[0], nc = c + d[1];
                        if (nr >= 0 && nr < size && nc >= 0 && nc < size) {
                            if (isValidMove(r, c, nr, nc)) return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public int[][] getBoard() { return board; }
    public int getSize() { return size; }
    public String getBoardType() { return boardType; }
}
