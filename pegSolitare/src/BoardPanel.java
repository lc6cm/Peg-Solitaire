import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BoardPanel extends JPanel {

    private static final int SIZE = 7;
    private static final int CELL = 60;
    private static final int PEG_RADIUS = 20;
    private static final int HOLE_RADIUS = 8;
    private static final int PADDING = 20;

    private int[][] board;
    private int selectedRow = -1;
    private int selectedCol = -1;

    private static final int[][] ENGLISH_TEMPLATE = {
        {0, 0, 1, 1, 1, 0, 0},
        {0, 0, 1, 1, 1, 0, 0},
        {1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 2, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1},
        {0, 0, 1, 1, 1, 0, 0},
        {0, 0, 1, 1, 1, 0, 0}
    };

    public BoardPanel() {
        board = new int[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                board[r][c] = ENGLISH_TEMPLATE[r][c];

        int panelSize = SIZE * CELL + PADDING * 2;
        setPreferredSize(new Dimension(panelSize, panelSize));
        setBackground(new Color(245, 230, 200));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
    }

    // Converts pixel coordinates to board row/col and handles selection
    private void handleClick(int pixelX, int pixelY) {
        int col = (pixelX - PADDING) / CELL;
        int row = (pixelY - PADDING) / CELL;

        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) return;
        if (board[row][col] == 0) return; // invalid cell

        if (board[row][col] == 1) {
            // Clicked a peg — select it (or deselect if already selected)
            if (selectedRow == row && selectedCol == col) {
                clearSelection();
            } else {
                selectedRow = row;
                selectedCol = col;
                onPegSelected(row, col);
            }
        } else if (board[row][col] == 2) {
            // Clicked an empty hole — pass to move handler
            if (selectedRow != -1) {
                onMoveAttempted(selectedRow, selectedCol, row, col);
            }
        }
        repaint();
    }

    // Override this in a subclass or expand here for game logic
    protected void onPegSelected(int row, int col) {
        // Hook for when a peg is selected — add game logic here later
        System.out.println("Peg selected at: [" + row + ", " + col + "]");
    }

    // Override this in a subclass or expand here for game logic
    protected void onMoveAttempted(int fromRow, int fromCol, int toRow, int toCol) {
        // Hook for when a move is attempted — add game logic here later
        System.out.println("Move attempted from [" + fromRow + ", " + fromCol +
                           "] to [" + toRow + ", " + toCol + "]");
        clearSelection();
    }

    public void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
        repaint();
    }

    public boolean hasSelection() {
        return selectedRow != -1;
    }

    public int getSelectedRow() { return selectedRow; }
    public int getSelectedCol() { return selectedCol; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int x = PADDING + c * CELL + CELL / 2;
                int y = PADDING + r * CELL + CELL / 2;

                if (board[r][c] == 0) continue;

                // Draw cell background
                g2.setColor(new Color(180, 120, 60));
                g2.fillRoundRect(
                    x - CELL / 2 + 4, y - CELL / 2 + 4,
                    CELL - 8, CELL - 8, 10, 10
                );

                boolean isSelected = (r == selectedRow && c == selectedCol);

                if (board[r][c] == 1) {
                    drawPeg(g2, x, y, isSelected);
                } else {
                    drawHole(g2, x, y);
                }
            }
        }
    }

    private void drawPeg(Graphics2D g2, int x, int y, boolean selected) {
        if (selected) {
            // Outer glow ring
            g2.setColor(new Color(255, 220, 50, 180));
            g2.fillOval(x - PEG_RADIUS - 5, y - PEG_RADIUS - 5,
                        (PEG_RADIUS + 5) * 2, (PEG_RADIUS + 5) * 2);
            // Bright peg body
            g2.setColor(new Color(200, 140, 30));
        } else {
            g2.setColor(new Color(60, 40, 20));
        }

        g2.fillOval(x - PEG_RADIUS, y - PEG_RADIUS,
                    PEG_RADIUS * 2, PEG_RADIUS * 2);

        // Highlight shine
        g2.setColor(selected ? new Color(255, 240, 120) : new Color(120, 80, 40));
        g2.fillOval(x - PEG_RADIUS + 5, y - PEG_RADIUS + 4,
                    PEG_RADIUS - 6, PEG_RADIUS - 6);
    }

    private void drawHole(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(100, 60, 20));
        g2.fillOval(x - HOLE_RADIUS, y - HOLE_RADIUS,
                    HOLE_RADIUS * 2, HOLE_RADIUS * 2);
    }

    public int getCell(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) return 0;
        return board[row][col];
    }

    public void setCell(int row, int col, int value) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) return;
        if (board[row][col] == 0) return;
        board[row][col] = value;
        repaint();
    }

    public int getBoardSize() { return SIZE; }
}