


public class AutomatedGame extends SolitaireGame {

    public AutomatedGame(int size, String boardType) {
        super(size, boardType);
    }

    @Override
    protected void initBoard() {
        int[][] template = BoardFactory.createBoard(boardType, size);
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                board[r][c] = template[r][c];
    }

    // Automated mode: finds the first valid move and executes it
    @Override
    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        return makeAutoMove();
    }

    // Finds and executes the first available valid move
    public boolean makeAutoMove() {
        int[][] dirs = {{-2,0},{2,0},{0,-2},{0,2}};
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board[r][c] == 1) {
                    for (int[] d : dirs) {
                        int nr = r + d[0], nc = c + d[1];
                        if (nr >= 0 && nr < size && nc >= 0 && nc < size) {
                            if (isValidMove(r, c, nr, nc)) {
                                executeMove(r, c, nr, nc);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false; // no move found
    }

    @Override
    public boolean isGameOver() {
        return !hasAnyValidMove();
    }

    @Override
    public String getStatusMessage() {
        if (isGameOver()) {
            int pegs = getPegCount();
            return pegs == 1
                ? "Automated solve complete! 1 peg remaining — perfect!"
                : "Automated solve finished. " + pegs + " pegs remaining.";
        }
        return "Auto mode running... Pegs: " + getPegCount();
    }
}
