package components;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FormPane extends JPanel {

  public FormPane() {
    setBorder(new EmptyBorder(8, 8, 8, 8));
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1;
    SearchPane searchPane = new SearchPane();
    add(searchPane, gbc);

    gbc.gridy++;
    ResultPane emailPane = new ResultPane();
    add(emailPane, gbc);
  }
}
