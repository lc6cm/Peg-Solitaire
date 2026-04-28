import java.io.*;
import java.util.*;

public class GameReplayer {
    private List<String> moves = new ArrayList<>();
    private String boardType;
    private int boardSize;
    private String gameMode;
    private int currentMove = 0;

    public void loadFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("MODE:")) {
                gameMode = line.substring(5);
            } else if (line.startsWith("TYPE:")) {
                boardType = line.substring(5);
            } else if (line.startsWith("SIZE:")) {
                boardSize = Integer.parseInt(line.substring(5));
            } else if (line.startsWith("SNAPSHOT:")) {
                moves.add(line);
            } else {
                moves.add(line);
            }
        }
        reader.close();
    }

    public boolean hasNextMove() {
        return currentMove < moves.size();
    }

    // Returns the raw string of the next entry
    public String getNextRawMove() {
        return moves.get(currentMove++);
    }

    
    public int[] getNextMove(String raw) {
        String[] parts = raw.split(",");
        return new int[]{
            Integer.parseInt(parts[0]),
            Integer.parseInt(parts[1]),
            Integer.parseInt(parts[2]),
            Integer.parseInt(parts[3])
        };
    }

    // Keep old getNextMove() for compatibility
    public int[] getNextMove() {
        return getNextMove(moves.get(currentMove++));
    }

    public String getBoardType() { return boardType; }
    public int getBoardSize()    { return boardSize; }
    public String getGameMode()  { return gameMode; }
}