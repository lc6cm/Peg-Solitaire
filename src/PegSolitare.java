import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;


public class PegSolitare extends JFrame {

    private BoardSizePanel boardSizePanel;
    private BoardTypePanel boardTypePanel;
    private GameModePanel  gameModePanel;
    private BoardPanel     boardPanel;
    private JButton        newGameButton;
    private JButton        nextMoveButton;
    private JButton        randomizeButton;
    private JTextArea      gameSettingsTextArea;
    private JCheckBox      recordCheckBox;
    private GameRecorder   recorder;
    private GameReplayer   replayer;

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

        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 5, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));

        recordCheckBox  = new JCheckBox("Record Game");
        newGameButton   = new JButton("New Game");
        nextMoveButton  = new JButton("Next Move");
        randomizeButton = new JButton("Randomize");
        JButton replayButton = new JButton("Replay File");

        newGameButton.setEnabled(false);
        nextMoveButton.setEnabled(false);

        newGameButton.addActionListener(e -> startNewGame());
        nextMoveButton.addActionListener(e -> doAutoMove());
        randomizeButton.addActionListener(e -> randomizeBoard());
        replayButton.addActionListener(e -> loadReplay());

        buttonPanel.add(recordCheckBox);
        buttonPanel.add(newGameButton);
        buttonPanel.add(nextMoveButton);
        buttonPanel.add(randomizeButton);
        buttonPanel.add(replayButton);
        
        JButton saveRecordingButton = new JButton("Save Recording");
        saveRecordingButton.addActionListener(e -> {
            if (recorder == null) {
                JOptionPane.showMessageDialog(this,
                    "No recording in progress. Tick 'Record Game' before starting.");
                return;
            }
            try {
                saveRecording();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save.");
            }
        });
        buttonPanel.add(saveRecordingButton);
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

        if (recordCheckBox.isSelected()) {
            recorder = new GameRecorder(boardType, size, mode);
            currentGame.setRecorder(recorder);
        } else {
            recorder = null;
        }

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
            try {
                saveRecording();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save recording.");
            }
            recorder = null;
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
                try {
                    saveRecording();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to save recording.");
                }
                recorder = null;
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
    
    private void saveRecording() throws IOException {
        if (recorder == null) return;
        try {
            // Create recordings folder if it doesn't exist
            java.io.File folder = new java.io.File("recordings");
            if (!folder.exists()) folder.mkdirs();

            // Generate a unique filename using time stamp
            String filename = "recordings/game_" + 
                System.currentTimeMillis() + ".txt";
            recorder.saveToFile(filename);
            JOptionPane.showMessageDialog(this,
                "Game saved to: " + filename);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save recording.");
        }
    }

    private void loadReplay() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                replayer = new GameReplayer();
                replayer.loadFromFile(chooser.getSelectedFile().getPath());

                currentGame = new ManualGame(replayer.getBoardSize(),
                                             replayer.getBoardType());
                boardPanel.setGame(currentGame);
                revalidate();
                pack();
                setLocationRelativeTo(null);

                javax.swing.Timer timer = new javax.swing.Timer(600, null);
                timer.addActionListener(e -> {
                    if (replayer.hasNextMove()) {
                        String next = replayer.getNextRawMove();

                        if (next.startsWith("SNAPSHOT:")) {
                            String data = next.substring(9);
                            String[] rows = data.split(";");
                            int[][] board = currentGame.getBoard();
                            for (int r = 0; r < rows.length; r++) {
                                String[] cols = rows[r].split(",");
                                for (int c = 0; c < cols.length; c++) {
                                    board[r][c] = Integer.parseInt(cols[c]);
                                }
                            }
                        } else {
                            int[] move = replayer.getNextMove(next);
                            currentGame.makeMove(move[0], move[1], move[2], move[3]);
                        }

                        boardPanel.repaint();
                        updateStatus();
                    } else {
                        timer.stop();
                        JOptionPane.showMessageDialog(this, "Replay complete.");
                    }
                });
                timer.start();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to load file.");
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PegSolitare::new);
    }
}