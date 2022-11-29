package components;

import controllers.SlangManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import utils.Constant;

public class FormPane extends JPanel {

  static SlangManager slangManager = new SlangManager();

  JComboBox<String> SearchTypeBox;
  JButton SearchButton;
  JTextField SearchField;
  JPanel SearchPanel;

  JTable table;
  JPanel resultPane;

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

  private void handleEvent() {
    SearchButton.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          String keyword = SearchField.getText();
          searchTable(slangManager.searchBySlang(keyword));
        }
      }
    );
  }

  private void prepareSearchUI() {
    SearchTypeBox = new JComboBox<String>(Constant.SEARCH_TYPES);

    SearchButton = new JButton("Search");
    SearchButton.setSize(100, 20);

    SearchField = new JTextField(20);
    SearchField.addActionListener(e -> SearchButton.doClick());

    SearchPanel = new JPanel(new FlowLayout());
    SearchPanel.add(new JLabel("Search for:"), BorderLayout.LINE_START);
    SearchPanel.add(SearchField, BorderLayout.LINE_END);

    JPanel searchTypePane = new JPanel(new FlowLayout());
    searchTypePane.add(new JLabel("By:"), BorderLayout.LINE_START);
    searchTypePane.add(SearchTypeBox);
    SearchPanel.add(searchTypePane);
    SearchPanel.add(SearchButton);

    SearchPanel.setBorder(
      new CompoundBorder(
        new TitledBorder("SEARCH"),
        new EmptyBorder(4, 4, 4, 4)
      )
    );

    add(SearchPanel);
  }

  private void prepareResultUI() {
    table = new JTable(getDataToTable(), Constant.TABLE_COLUMNS_NAME);
    resultPane = new JPanel(new BorderLayout());
    resultPane.add(new JScrollPane(table), BorderLayout.CENTER);
    resultPane.setBorder(
      new CompoundBorder(
        new TitledBorder("RESULT"),
        new EmptyBorder(4, 4, 4, 4)
      )
    );

    add(resultPane);
  }

  public FormPane() {
    setBorder(new EmptyBorder(8, 8, 8, 8));
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1;
    prepareSearchUI();

    gbc.gridy++;
    prepareResultUI();

    handleEvent();
  }
}
