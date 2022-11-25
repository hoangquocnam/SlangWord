package components;

import java.awt.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class SearchPane extends JPanel {

  JComboBox<String> SearchTypeBox;
  JButton SearchButton;
  static String[] SEARCH_TYPES = { "Definition", "Slang", "All" };

  public void prepareTypeBox() {
    SearchTypeBox = new JComboBox<String>(SEARCH_TYPES);
  }

  public SearchPane() {
    prepareTypeBox();
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    // KEYWORD
    gbc.gridx = 0;
    gbc.gridy = 0;
    add(new JLabel("Search for:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    add(new JTextField(50), gbc);

    //SELECT SEARCH TYPE
    gbc.gridy = 1;
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.weightx = 0;
    gbc.fill = GridBagConstraints.NONE;
    add(new JLabel("By:"), gbc);

    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx++;
    gbc.weightx = 1;
    add(SearchTypeBox, gbc);

    //BUTTON
    SearchButton = new JButton("Search");
    gbc.gridy = 3;
    gbc.gridx = 2;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.weightx = 1;
    add(SearchButton, gbc);

    setBorder(
      new CompoundBorder(
        new TitledBorder("SEARCH"),
        new EmptyBorder(4, 4, 4, 4)
      )
    );
  }
}