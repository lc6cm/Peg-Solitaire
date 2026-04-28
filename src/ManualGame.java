/**
 * Manual game mode — a human player makes all moves.
 * Extends SolitaireGame with move validation and game-over detection.
 */
public class ManualGame extends SolitaireGame {

    private String lastMoveMessage = "Select a peg to begin.";

    public ManualGame(int size, String boardType) {
        super(size, boardType);
    }

    @Override
    protected void initBoard() {
        int[][] template = BoardFactory.createBoard(boardType, size);
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                board[r][c] = template[r][c];
    }

    @Override
    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isValidMove(fromRow, fromCol, toRow, toCol)) {
            lastMoveMessage = "Invalid move. Try again.";
            return false;
        }
        executeMove(fromRow, fromCol, toRow, toCol);
        lastMoveMessage = "Move made: [" + fromRow + "," + fromCol +
                          "] → [" + toRow + "," + toCol + "]";
        return true;
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
                ? "You win! Only 1 peg remaining."
                : "Game over! " + pegs + " pegs remaining.";
        }
        return lastMoveMessage + " | Pegs: " + getPegCount();
    }
}
