import java.io.*;
import java.util.*;

public class GameReplayer {
    private List<int[]> moves = new ArrayList<>();
    private String boardType;
    private int boardSize;
    private String gameMode;
    private int currentMove = 0;

    public void loadFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue; // skip blank lines
            if (line.startsWith("MODE:")) {
                gameMode = line.substring(5);
            } else if (line.startsWith("TYPE:")) {
                boardType = line.substring(5);
            } else if (line.startsWith("SIZE:")) {
                boardSize = Integer.parseInt(line.substring(5));
            } else {
                String[] parts = line.split(",");
                if (parts.length == 4) { // only parse lines with exactly 4 values
                    moves.add(new int[]{
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])
                    });
                }
            }
        }
        reader.close();
    }
    public boolean hasNextMove() {
        return currentMove < moves.size();
    }

    public int[] getNextMove() {
        return moves.get(currentMove++);
    }

    public String getBoardType() { return boardType; }
    public int getBoardSize()    { return boardSize; }
    public String getGameMode()  { return gameMode; }
}