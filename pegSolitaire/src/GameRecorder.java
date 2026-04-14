import java.io.*;
import java.util.*;

public class GameRecorder {
    private List<String> moves = new ArrayList<>();
    private String boardType;
    private int boardSize;
    private String gameMode;

    public GameRecorder(String boardType, int boardSize, String gameMode) {
        this.boardType = boardType;
        this.boardSize = boardSize;
        this.gameMode  = gameMode;
    }

    public void recordMove(int fromRow, int fromCol, int toRow, int toCol) {
        moves.add(fromRow + "," + fromCol + "," + toRow + "," + toCol);
    }

    public void saveToFile(String filename) throws IOException {
        new java.io.File(filename).getParentFile().mkdirs(); // creates folder if missing
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        writer.println("MODE:" + gameMode);
        writer.println("TYPE:" + boardType);
        writer.println("SIZE:" + boardSize);
        for (String move : moves) {
            writer.println(move);
        }
        writer.close();
    }
    public void recordRandomize(int[][] board, int size) {
        moves.add("RANDOMIZE");
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                moves.add(r + "," + c + "," + board[r][c]);
            }
        }
    }
}
