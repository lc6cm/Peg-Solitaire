import java.awt.GridLayout;

import javax.swing.*;

/**
 * Panel for selecting game mode: Manual or Automated.
 */
public class GameModePanel extends JPanel {

    private JRadioButton manual, automated;
    private ButtonGroup group;

    public GameModePanel() {
        setLayout(new GridLayout(2, 1));
        setBorder(javax.swing.BorderFactory.createTitledBorder("Game Mode"));

        manual   = new JRadioButton("Manual", true);
        automated = new JRadioButton("Automated", false);

        group = new ButtonGroup();
        group.add(manual);
        group.add(automated);

        add(manual);
        add(automated);
    }

    public String getGameMode() {
        return automated.isSelected() ? "Automated" : "Manual";
    }

    public boolean isAutomated() {
        return automated.isSelected();
    }
}
