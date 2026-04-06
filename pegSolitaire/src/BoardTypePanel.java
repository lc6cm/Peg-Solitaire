import java.awt.GridLayout;
import javax.swing.*;

/**
 * Panel for selecting board type (English, Hexagon, Diamond).
 * Fixed: only English starts selected by default.
 */
public class BoardTypePanel extends JPanel {

    JRadioButton english, hexagon, diamond;
    private ButtonGroup group;

    public BoardTypePanel() {
        setLayout(new GridLayout(3, 1));
        setBorder(javax.swing.BorderFactory.createTitledBorder("Board Type"));

        english = new JRadioButton("English", true);   // default
        hexagon = new JRadioButton("Hexagon", false);
        diamond = new JRadioButton("Diamond", false);

        group = new ButtonGroup();
        group.add(english);
        group.add(hexagon);
        group.add(diamond);

        add(english);
        add(hexagon);
        add(diamond);
    }

    public String getBoardType() {
        if (hexagon.isSelected()) return "Hexagon";
        if (diamond.isSelected()) return "Diamond";
        return "English";
    }
}
