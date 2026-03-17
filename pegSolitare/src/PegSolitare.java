import java.awt.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;




public class PegSolitare extends JFrame{
	private BoardSizePanel boardSizePanel;
	private BoardTypePanel boardTypePanel;
	private BoardPanel boardPanel;
	private JButton newGameButton;
	private JTextArea gameSettingsTextArea;
	
	public PegSolitare() {
		setTitle("Peg Solitare");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(100,50));
		
		
		//Initialize board Size panel
		JPanel northPanel = new JPanel(new GridLayout(1, 1));
		boardSizePanel = new BoardSizePanel();
		
		
		//Initialize center panel
		JPanel centerPanel = new JPanel(new GridLayout(1,3));
		
		boardTypePanel = new BoardTypePanel();
		boardPanel = new BoardPanel();
		
		//Initialize action listener for new game button
		newGameButton = new JButton("New Game");
		newGameButton.addActionListener(new newGameButtonListener());
		gameSettingsTextArea = new JTextArea(5,20);
		gameSettingsTextArea.setEditable(false);
		
		//Add panels into north panel
		northPanel.add(boardSizePanel);
		//Add northPanel into the main window
		add(northPanel, BorderLayout.NORTH);
		
		//Add panels into center panel
		centerPanel.add(boardTypePanel);
		centerPanel.add(boardPanel,BorderLayout.CENTER);
		//add centerPanel to the main window
		add(centerPanel, BorderLayout.CENTER);
		
		add(newGameButton,BorderLayout.EAST);
		add(gameSettingsTextArea, BorderLayout.SOUTH);
		
			
		pack();
		setVisible(true);
	}
	
	
	
	public static void main(String[] args) {
		new PegSolitare();

	}

	private class newGameButtonListener implements ActionListener  {
			
			public void actionPerformed(ActionEvent e) {
				
				String boardSize = boardSizePanel.getBoardSize();
				String boardType = boardTypePanel.getBoardType();
				
				boardPanel = new BoardPanel();
				
				gameSettingsTextArea.setText(String.format(
						"=== New Game Started ===\n" +
					            "Board Type: " + boardType + "\n" +
					            "Board Size: " + boardSize + "\n" +
					            "Total Pegs: 32\n" +
					            "Starting Hole: Center\n" +
					            "Just test info for later^^"
					        ));
				
			}
		}
}
