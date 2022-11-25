package components;

import java.awt.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ResultPane extends JPanel {

  public ResultPane() {
    JPanel detailsPane = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridwidth = GridBagConstraints.REMAINDER;
    detailsPane.add(new JScrollPane(new JList()), gbc);
    JPanel buttonsPane = new JPanel(new GridBagLayout());
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    buttonsPane.add(new JButton("Add"), gbc);
    gbc.gridy++;
    buttonsPane.add(new JButton("Edit"), gbc);
    gbc.gridy++;
    buttonsPane.add(new JButton("Delete"), gbc);
    gbc.gridy++;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.NORTH;
    buttonsPane.add(new JButton("As Default"), gbc);
    JPanel formatPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
    formatPane.setBorder(
      new TitledBorder(new EmptyBorder(1, 1, 1, 1), "Format:")
    );
    formatPane.add(new JRadioButton("Aa"));
    formatPane.add(new JRadioButton("[ab]"));
    setLayout(new BorderLayout());
    add(detailsPane);
    add(buttonsPane, BorderLayout.LINE_END);
    add(formatPane, BorderLayout.PAGE_END);

    setBorder(
      new CompoundBorder(
        new TitledBorder("RESULT"),
        new EmptyBorder(4, 4, 4, 4)
      )
    );
  }
}
