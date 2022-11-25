package components;

import java.awt.*;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class SearchPane extends JPanel {

  JComboBox<String> SearchTypeBox;

  String[] SEARCH_TYPES = { "Definition", "Slang" , "All" };
  public void prepareTypeBox() {
    SearchTypeBox = new JComboBox<String>(SEARCH_TYPES);
  }

  public SearchPane() {
    prepareTypeBox();
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    add(new JLabel("Search for:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    add(new JTextField(50), gbc);

    gbc.gridy = 2;
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.weightx = 0;
    gbc.fill = GridBagConstraints.NONE;
    add(new JLabel("By:"), gbc);
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx++;
    gbc.weightx = 1;
    add(SearchTypeBox, gbc);
    setBorder(
      new CompoundBorder(
        new TitledBorder("SEARCH"),
        new EmptyBorder(4, 4, 4, 4)
      )
    );
  }
}
