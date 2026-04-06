import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class BoardSizePanel extends JPanel {
    private JTextField boardSizeField;
    private JLabel validationLabel;
    private ValidationListener validationListener;

    public interface ValidationListener {
        void onValidationChanged();
    }

    public void setValidationListener(ValidationListener listener) {
        this.validationListener = listener;
    }

    public BoardSizePanel() {
        setLayout(new GridLayout(3, 1));
        add(new JLabel("Board Size (odd 5-15):"));

        boardSizeField = new JTextField(5);

        boardSizeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                    return;
                }
                if (boardSizeField.getText().length() >= 2) {
                    e.consume();
                }
            }
        });

        add(boardSizeField);

        validationLabel = new JLabel(" ");
        validationLabel.setForeground(Color.RED);
        add(validationLabel);

        boardSizeField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { checkInput(); }
            public void removeUpdate(DocumentEvent e)  { checkInput(); }
            public void changedUpdate(DocumentEvent e) { checkInput(); }
        });
    }

    private void checkInput() {
        String text = boardSizeField.getText().trim();
        boolean valid = isValidSize(text);

        if (text.isEmpty()) {
            validationLabel.setText("Required");
            validationLabel.setForeground(Color.RED);
        } else if (!valid) {
            validationLabel.setText("Must be odd number: 5, 7, 9, 11, 13, 15");
            validationLabel.setForeground(Color.RED);
        } else {
            validationLabel.setText("✓ Valid");
            validationLabel.setForeground(new Color(0, 140, 0));
        }

        if (validationListener != null) validationListener.onValidationChanged();
    }

    public boolean isValidSize(String text) {
        try {
            int val = Integer.parseInt(text.trim());
            return val >= 5 && val <= 15 && val % 2 == 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isValid() {
        if (boardSizeField == null) return false;
        return isValidSize(boardSizeField.getText());
    }

    public int getBoardSizeValue() {
        try {
            return Integer.parseInt(boardSizeField.getText().trim());
        } catch (NumberFormatException e) {
            return 7;
        }
    }

    public String getBoardSize() {
        return boardSizeField.getText().trim();
    }
}