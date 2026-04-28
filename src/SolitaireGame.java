public abstract class SolitaireGame {

    protected int[][] board;
    protected int size;
    protected String boardType;
    private GameRecorder recorder;

    public SolitaireGame(int size, String boardType) {
        this.size = size;
        this.boardType = boardType;
        this.board = new int[size][size];
        initBoard();
    }

    protected abstract void initBoard();

    public abstract boolean makeMove(int fromRow, int fromCol, int toRow, int toCol);

    public abstract boolean isGameOver();

    public abstract String getStatusMessage();

    public void setRecorder(GameRecorder recorder) {
        this.recorder = recorder;
    }

    public void randomizeBoard() {
        java.util.Random rand = new java.util.Random();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board[r][c] != 0) {
                    board[r][c] = rand.nextBoolean() ? 1 : 2;
                }
            }
        }
        // Record the full board state after randomizing
        if (recorder != null) {
            recorder.recordSnapshot(board, size);
        }
    }

    protected boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Bounds check first before touching the array
        if (fromRow < 0 || fromRow >= size || fromCol < 0 || fromCol >= size) return false;
        if (toRow < 0   || toRow >= size   || toCol < 0   || toCol >= size)   return false;

        if (board[fromRow][fromCol] != 1) return false;
        if (board[toRow][toCol] != 2) return false;

        int dRow = toRow - fromRow;
        int dCol = toCol - fromCol;

        if (!((Math.abs(dRow) == 2 && dCol == 0) || (Math.abs(dCol) == 2 && dRow == 0))) {
            return false;
        }

        int midRow = fromRow + dRow / 2;
        int midCol = fromCol + dCol / 2;

        return board[midRow][midCol] == 1;
    }

    protected void executeMove(int fromRow, int fromCol, int toRow, int toCol) {
        int midRow = fromRow + (toRow - fromRow) / 2;
        int midCol = fromCol + (toCol - fromCol) / 2;
        board[fromRow][fromCol] = 2;
        board[midRow][midCol]   = 2;
        board[toRow][toCol]     = 1;

        if (recorder != null) {
            recorder.recordMove(fromRow, fromCol, toRow, toCol);
        }
    }

    public int getPegCount() {
        int count = 0;
        for (int[] row : board)
            for (int cell : row)
                if (cell == 1) count++;
        return count;
    }

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