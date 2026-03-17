import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BoardSizePanel extends JPanel{
	private JTextField boardSize;
	
	public BoardSizePanel() {
		setLayout(new GridLayout(1,2));
		add(new JLabel("Board Size: "));
		boardSize = new JTextField(15);
		add(boardSize);
		
	}
		
		
	public String getBoardSize() {
		return boardSize.getText();
	}

}
