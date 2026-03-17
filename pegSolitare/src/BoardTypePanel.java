import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class BoardTypePanel extends JPanel {
	JRadioButton english, hexagon, diamond;
	private ButtonGroup group;

	public BoardTypePanel() {
		setLayout(new GridLayout(3, 1));
		setBorder(BorderFactory.createTitledBorder("Board Type"));

		english = new JRadioButton("English", true);
		hexagon = new JRadioButton("Hexagon", true);
		diamond = new JRadioButton("Diamond", true);

		group = new ButtonGroup();
		group.add(english);
		group.add(hexagon);
		group.add(diamond);

		add(english);
		add(hexagon);
		add(diamond);

	}

	public String getBoardType() {
		if (english.isSelected()) {
			return "English";
		}
		else if(hexagon.isSelected()) {
			return "Hexagon";
		}
		else if(diamond.isSelected()) {
			return "Diamond";
		
		}
		return "English"; // default value if nothing is selected
	}
	
	

	}
