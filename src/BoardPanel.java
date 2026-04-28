import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BoardPanel extends JPanel {

    private static final int CELL = 60;
    private static final int PEG_RADIUS = 20;
    private static final int HOLE_RADIUS = 8;
    private static final int PADDING = 20;

    private SolitaireGame game;
    private int selectedRow = -1;
    private int selectedCol = -1;

    private MoveListener moveListener;

    public interface MoveListener {
        void onMoveMade();
    }

    public void setMoveListener(MoveListener listener) {
        this.moveListener = listener;
    }

    public BoardPanel(SolitaireGame game) {
        this.game = game;
        updatePreferredSize();
        setBackground(new Color(245, 230, 200));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
    }

    public void setGame(SolitaireGame game) {
        this.game = game;
        clearSelection();
        updatePreferredSize();
        repaint();
    }

    private void updatePreferredSize() {
        int panelSize = game.getSize() * CELL + PADDING * 2;
        Dimension d = new Dimension(panelSize, panelSize);
        setPreferredSize(d);
    }

    private void handleClick(int pixelX, int pixelY) {
        if (game == null || game.isGameOver()) return;

        int col = (pixelX - PADDING) / CELL;
        int row = (pixelY - PADDING) / CELL;
        int size = game.getSize();

        if (row < 0 || row >= size || col < 0 || col >= size) return;

        int[][] board = game.getBoard();
        if (board[row][col] == 0) return;

        if (board[row][col] == 1) {
            if (selectedRow == row && selectedCol == col) {
                clearSelection();
            } else {
                selectedRow = row;
                selectedCol = col;
            }
        } else if (board[row][col] == 2 && selectedRow != -1) {
            boolean moved = game.makeMove(selectedRow, selectedCol, row, col);
            clearSelection();
            if (moved && moveListener != null) {
                moveListener.onMoveMade();
            }
        }
        repaint();
    }

    public void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
        repaint();
    }

    public boolean hasSelection() {
        return selectedRow != -1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (game == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int[][] board = game.getBoard();
        int size = game.getSize();

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                int x = PADDING + c * CELL + CELL / 2;
                int y = PADDING + r * CELL + CELL / 2;

                if (board[r][c] == 0) continue;

                g2.setColor(new Color(180, 120, 60));
                g2.fillRoundRect(x - CELL / 2 + 4, y - CELL / 2 + 4,
                                 CELL - 8, CELL - 8, 10, 10);

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
            g2.setColor(new Color(255, 220, 50, 180));
            g2.fillOval(x - PEG_RADIUS - 5, y - PEG_RADIUS - 5,
                        (PEG_RADIUS + 5) * 2, (PEG_RADIUS + 5) * 2);
            g2.setColor(new Color(200, 140, 30));
        } else {
            g2.setColor(new Color(60, 40, 20));
        }
        g2.fillOval(x - PEG_RADIUS, y - PEG_RADIUS, PEG_RADIUS * 2, PEG_RADIUS * 2);
        g2.setColor(selected ? new Color(255, 240, 120) : new Color(120, 80, 40));
        g2.fillOval(x - PEG_RADIUS + 5, y - PEG_RADIUS + 4,
                    PEG_RADIUS - 6, PEG_RADIUS - 6);
    }

    private void drawHole(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(100, 60, 20));
        g2.fillOval(x - HOLE_RADIUS, y - HOLE_RADIUS,
                    HOLE_RADIUS * 2, HOLE_RADIUS * 2);
    }
}