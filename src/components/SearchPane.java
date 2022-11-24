package components;

import java.awt.*;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchPane extends JPanel {

  JComboBox<String> searchTypeBox;

  public void prepareTypeBox() {
    String[] searchTypes = {"English", "Slang"};
    searchTypeBox = new JComboBox<String>(searchTypes);
  }

  public SearchPane() {
    prepareTypeBox();
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.EAST;
    add(new JLabel("Search for:"), gbc);
    gbc.gridy++;
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.weightx = 0.5;
    add(new JTextField(10), gbc);
    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridy++;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.weightx = 0;
    gbc.fill = GridBagConstraints.NONE;
    add(new JLabel("By:"), gbc);
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx++;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    add(searchTypeBox, gbc);
  }
}
