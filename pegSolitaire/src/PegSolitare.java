import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PegSolitare extends JFrame {

    private BoardSizePanel boardSizePanel;
    private BoardTypePanel boardTypePanel;
    private GameModePanel  gameModePanel;
    private BoardPanel     boardPanel;
    private JButton        newGameButton;
    private JButton        nextMoveButton;
    private JButton        randomizeButton;
    private JTextArea      gameSettingsTextArea;

    private SolitaireGame  currentGame;

    public PegSolitare() {
        setTitle("Peg Solitaire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        boardSizePanel = new BoardSizePanel();
        boardTypePanel = new BoardTypePanel();
        gameModePanel  = new GameModePanel();

        boardSizePanel.setValidationListener(this::updateNewGameButton);

        northPanel.add(boardSizePanel);
        northPanel.add(boardTypePanel);
        northPanel.add(gameModePanel);
        add(northPanel, BorderLayout.NORTH);

        currentGame = new ManualGame(7, "English");
        boardPanel = new BoardPanel(currentGame);
        boardPanel.setMoveListener(this::onMoveMade);

        JPanel centerWrapper = new JPanel(new FlowLayout());
        centerWrapper.add(boardPanel);
        add(new JScrollPane(centerWrapper), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));

        newGameButton   = new JButton("New Game");
        nextMoveButton  = new JButton("Next Move");
        randomizeButton = new JButton("Randomize");

        newGameButton.setEnabled(false);
        nextMoveButton.setEnabled(false);

        newGameButton.addActionListener(e -> startNewGame());
        nextMoveButton.addActionListener(e -> doAutoMove());
        randomizeButton.addActionListener(e -> randomizeBoard());

        buttonPanel.add(newGameButton);
        buttonPanel.add(nextMoveButton);
        buttonPanel.add(randomizeButton);
        add(buttonPanel, BorderLayout.EAST);

        gameSettingsTextArea = new JTextArea(5, 30);
        gameSettingsTextArea.setEditable(false);
        gameSettingsTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        gameSettingsTextArea.setText("Configure settings above and click New Game.");
        add(new JScrollPane(gameSettingsTextArea), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        updateNewGameButton();
    }

    private void updateNewGameButton() {
        newGameButton.setEnabled(boardSizePanel.isValid());
    }

    private void startNewGame() {
        int size         = boardSizePanel.getBoardSizeValue();
        String boardType = boardTypePanel.getBoardType();
        String mode      = gameModePanel.getGameMode();
        boolean auto     = gameModePanel.isAutomated();

        currentGame = auto
            ? new AutomatedGame(size, boardType)
            : new ManualGame(size, boardType);

        boardPanel.setGame(currentGame);

        nextMoveButton.setEnabled(auto);

        gameSettingsTextArea.setText(
            "=== New Game Started ===\n" +
            "Mode:       " + mode      + "\n" +
            "Board Type: " + boardType + "\n" +
            "Board Size: " + size + "x" + size + "\n" +
            "Total Pegs: " + currentGame.getPegCount() + "\n" +
            "Status:     " + currentGame.getStatusMessage()
        );

        revalidate();
        pack();
        setLocationRelativeTo(null);
        repaint();
    }

    private void onMoveMade() {
        updateStatus();
        if (currentGame.isGameOver()) {
            nextMoveButton.setEnabled(false);
            JOptionPane.showMessageDialog(this,
                currentGame.getStatusMessage(), "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void doAutoMove() {
        if (currentGame instanceof AutomatedGame) {
            boolean moved = ((AutomatedGame) currentGame).makeAutoMove();
            boardPanel.repaint();
            updateStatus();
            if (!moved || currentGame.isGameOver()) {
                nextMoveButton.setEnabled(false);
                JOptionPane.showMessageDialog(this,
                    currentGame.getStatusMessage(), "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void randomizeBoard() {
        if (currentGame == null) return;
        currentGame.randomizeBoard();
        boardPanel.repaint();
        updateStatus();
    }

    private void updateStatus() {
        gameSettingsTextArea.setText(
            "Mode:       " + gameModePanel.getGameMode()  + "\n" +
            "Board Type: " + currentGame.getBoardType()   + "\n" +
            "Pegs left:  " + currentGame.getPegCount()    + "\n" +
            "Status:     " + currentGame.getStatusMessage()
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PegSolitare::new);
    }
}