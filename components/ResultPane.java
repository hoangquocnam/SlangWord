package components;

import controllers.SlangManager;
import java.awt.*;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import utils.Constant;

public class ResultPane extends JPanel {

  static SlangManager slangManager = new SlangManager();

  JTable table;

  private static String[][] getDataToTable() {
    TreeMap<String, ArrayList<String>> data = slangManager.getAllSlang();
    String[][] dataArr = new String[slangManager.getAllSlang().size()][3];

    int i = 0;
    for (String key : data.keySet()) {
      dataArr[i][0] = String.valueOf(i + 1);
      dataArr[i][1] = key;
      dataArr[i][2] = String.join(", ", data.get(key));
      i++;
    }

    return dataArr;
  }

  private void prepareUI() {
    table = new JTable(getDataToTable(), Constant.TABLE_COLUMNS_NAME);
  }

  public void searchTable(TreeMap<String, ArrayList<String>> data) {
    String[][] dataArr = new String[data.size()][3];

    int i = 0;
    for (String key : data.keySet()) {
      dataArr[i][0] = String.valueOf(i + 1);
      dataArr[i][1] = key;
      dataArr[i][2] = String.join(", ", data.get(key));
      i++;
    }

    table.setModel(
      new javax.swing.table.DefaultTableModel(
        dataArr,
        Constant.TABLE_COLUMNS_NAME
      )
    );
  }

  public ResultPane() {
    prepareUI();
    JPanel detailsPane = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridwidth = GridBagConstraints.REMAINDER;
    detailsPane.add(new JScrollPane(table));
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
