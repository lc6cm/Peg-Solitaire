/**
 * Generates board templates for different board types and sizes.
 * 0 = invalid cell, 1 = peg, 2 = empty hole
 */
public class BoardFactory {

    public static int[][] createBoard(String type, int size) {
        switch (type) {
            case "Hexagon": return createHexagonBoard(size);
            case "Diamond": return createDiamondBoard(size);
            default:        return createEnglishBoard(size);
        }
    }

    // English: cross/plus shape — corners cut off
    private static int[][] createEnglishBoard(int size) {
        int[][] board = new int[size][size];
        int cut = size / 3; // how many cols/rows to cut from each corner

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                boolean inCorner = (r < cut || r >= size - cut) &&
                                   (c < cut || c >= size - cut);
                board[r][c] = inCorner ? 0 : 1;
            }
        }
        // Center hole
        board[size / 2][size / 2] = 2;
        return board;
    }

    // Hexagon: rows widen then narrow like a hexagon
    private static int[][] createHexagonBoard(int size) {
        int[][] board = new int[size][size];
        int mid = size / 2;

        for (int r = 0; r < size; r++) {
            int offset = Math.abs(r - mid);
            for (int c = 0; c < size; c++) {
                board[r][c] = (c >= offset && c < size - offset) ? 1 : 0;
            }
        }
        board[size / 2][size / 2] = 2;
        return board;
    }

    // Diamond: rotated square shape
    private static int[][] createDiamondBoard(int size) {
        int[][] board = new int[size][size];
        int mid = size / 2;

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                board[r][c] = (Math.abs(r - mid) + Math.abs(c - mid) <= mid) ? 1 : 0;
            }
        }
        board[size / 2][size / 2] = 2;
        return board;
    }
}
