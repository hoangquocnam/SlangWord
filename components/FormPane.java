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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import utils.Constant;

public class FormPane extends JPanel {

  static SlangManager slangManager = new SlangManager();

  // LEFT
  JPanel LeftPanel;
  JPanel SearchPanel;
  JComboBox<String> SearchTypeBox;
  JButton SearchButton;
  JTextField SearchField;

  JPanel HistoryPane;
  JTable HistoryTable;

  JPanel ControlPane;
  JButton AddButton;
  JButton EditButton;
  JButton DeleteButton;

  JTextField ControlSlangField;
  JTextField ControlDefinitionField;

  // RIGHT
  JPanel ResultPane;
  JTable TableResult;

  private static String[][] getDataToTable(
    TreeMap<String, ArrayList<String>> data
  ) {
    String[][] dataArr = new String[slangManager.getAllSlang().size()][3];

    int i = 0;
    for (String key : data.keySet()) {
      dataArr[i][0] = String.valueOf(i + 1);
      dataArr[i][1] = key;
      dataArr[i][2] =
        String.join(Constant.TABLE_DEFINITION_SEPARATOR, data.get(key));
      i++;
    }

    return dataArr;
  }

  public String[][] searchTable(TreeMap<String, ArrayList<String>> data) {
    String[][] dataArr = new String[data.size()][3];

    int i = 0;
    for (String key : data.keySet()) {
      dataArr[i][0] = String.valueOf(i + 1);
      dataArr[i][1] = key;
      dataArr[i][2] =
        String.join(Constant.TABLE_DEFINITION_SEPARATOR, data.get(key));
      i++;
    }

    TableResult.setModel(
      new javax.swing.table.DefaultTableModel(
        dataArr,
        Constant.TABLE_COLUMNS_NAME
      )
    );
    return dataArr;
  }

  public String[][] historySearchTable() {
    slangManager.loadHistory();
    TreeMap<String, ArrayList<String>> data = slangManager.getHistorySlang();
    String[][] dataArr = new String[data.size()][3];

    int i = 0;
    for (String key : data.keySet()) {
      dataArr[i][0] = String.valueOf(i + 1);
      dataArr[i][1] = key;
      dataArr[i][2] =
        String.join(Constant.TABLE_DEFINITION_SEPARATOR, data.get(key));
      i++;
    }

    HistoryTable.setModel(
      new javax.swing.table.DefaultTableModel(
        dataArr,
        Constant.TABLE_COLUMNS_NAME
      )
    );
    return dataArr;
  }

  private void addEvents() {
    SearchButton.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          String keyword = SearchField.getText();
          String type = SearchTypeBox.getSelectedItem().toString();
          if (type.equals(Constant.SEARCH_TYPES[0])) {
            searchTable(slangManager.searchBySlang(keyword, true));
          } else if (type.equals(Constant.SEARCH_TYPES[1])) {
            searchTable(slangManager.searchByDefinition(keyword, true));
          } else if (type.equals(Constant.SEARCH_TYPES[2])) {
            searchTable(slangManager.searchByAll(keyword, true));
          }
        }
      }
    );

    SearchTypeBox.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          String keyword = SearchField.getText();
          String type = SearchTypeBox.getSelectedItem().toString();
          if (type.equals(Constant.SEARCH_TYPES[0])) {
            searchTable(slangManager.searchBySlang(keyword, false));
          } else if (type.equals(Constant.SEARCH_TYPES[1])) {
            searchTable(slangManager.searchByDefinition(keyword, false));
          } else if (type.equals(Constant.SEARCH_TYPES[2])) {
            searchTable(slangManager.searchByAll(keyword, false));
          }
        }
      }
    );

    SearchField
      .getDocument()
      .addDocumentListener(
        new DocumentListener() {
          public void changedUpdate(DocumentEvent e) {
            SearchButton.doClick();
          }

          public void removeUpdate(DocumentEvent e) {
            SearchButton.doClick();
          }

          public void insertUpdate(DocumentEvent e) {
            SearchButton.doClick();
          }
        }
      );

    // Table on select row
    TableResult
      .getSelectionModel()
      .addListSelectionListener(
        new ListSelectionListener() {
          public void valueChanged(ListSelectionEvent event) {
            if (TableResult.getSelectedRow() > -1) {
              ControlSlangField.setText(
                TableResult
                  .getValueAt(TableResult.getSelectedRow(), 1)
                  .toString()
              );
              ControlDefinitionField.setText(
                TableResult
                  .getValueAt(TableResult.getSelectedRow(), 2)
                  .toString()
              );
            }
          }
        }
      );

    AddButton.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          String slang = ControlSlangField.getText();
          boolean isExist = slangManager.isExistSlang(slang);
          if (isExist) {
            int result = JOptionPane.showConfirmDialog(
              null,
              "Do you want to override it?",
              "This slang is already exist",
              JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
              String definition = ControlDefinitionField.getText();
              ArrayList<String> definitions = new ArrayList<String>();
              definitions.add(definition);
              slangManager.addSlang(slang, definitions, true);
              searchTable(slangManager.getAllSlang());
            } else if (result == JOptionPane.NO_OPTION) {
              String definition = ControlDefinitionField.getText();
              ArrayList<String> definitions = new ArrayList<String>();
              definitions.add(definition);
              slangManager.addSlang(slang, definitions, false);
              searchTable(slangManager.getAllSlang());
            }
          } else {
            String definition = ControlDefinitionField.getText();
            ArrayList<String> definitions = new ArrayList<String>();
            definitions.add(definition);
            slangManager.addSlang(slang, definitions, true);
            searchTable(slangManager.getAllSlang());
          }
          ControlSlangField.setText("");
          ControlDefinitionField.setText("");
        }
      }
    );

    EditButton.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          String slang = ControlSlangField.getText();
          String definition = ControlDefinitionField.getText();
          ArrayList<String> definitions = new ArrayList<String>();
          definitions.add(definition);
          slangManager.editSlang(slang, definitions);
          searchTable(slangManager.getAllSlang());
          ControlSlangField.setText("");
          ControlDefinitionField.setText("");
        }
      }
    );

    DeleteButton.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          String slang = ControlSlangField.getText();
          int result = JOptionPane.showConfirmDialog(
            null,
            "Do you want to delete it?",
            "Delete slang",
            JOptionPane.YES_NO_OPTION
          );

          if (result == JOptionPane.YES_OPTION) {
            slangManager.removeSlang(slang);
            searchTable(slangManager.getAllSlang());
            ControlSlangField.setText("");
            ControlDefinitionField.setText("");
          }
        }
      }
    );
  }

  private void prepareSearchUI() {
    SearchTypeBox = new JComboBox<String>(Constant.SEARCH_TYPES);

    SearchButton = new JButton("Search");

    SearchField = new JTextField(20);
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
  }

  private void prepareResultUI() {
    TreeMap<String, ArrayList<String>> data = slangManager.getAllSlang();
    TableResult = new JTable(getDataToTable(data), Constant.TABLE_COLUMNS_NAME);
    ResultPane = new JPanel(new BorderLayout());
    JScrollPane scrollPane = new JScrollPane(
      TableResult,
      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );
    ResultPane.add(scrollPane, BorderLayout.CENTER);
    ResultPane.setBorder(
      new CompoundBorder(
        new TitledBorder("RESULT"),
        new EmptyBorder(4, 4, 4, 4)
      )
    );
  }

  private void prepareHistoryUI() {
    TreeMap<String, ArrayList<String>> dataHistory = slangManager.getHistorySlang();
    HistoryTable =
      new JTable(getDataToTable(dataHistory), Constant.TABLE_COLUMNS_NAME);
    HistoryPane = new JPanel(new BorderLayout());
    JScrollPane scrollPane = new JScrollPane(
      HistoryTable,
      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );
    HistoryPane.add(scrollPane, BorderLayout.CENTER);
    HistoryPane.setBorder(
      new CompoundBorder(
        new TitledBorder("HISTORY"),
        new EmptyBorder(4, 4, 4, 4)
      )
    );
  }

  private void prepareControlUI() {
    ControlPane = new JPanel();
    ControlPane.setSize(100, 50);
    ControlPane.setLayout(new GridLayout(2, 1));

    JPanel FieldContainer = new JPanel(new GridLayout(2, 1));

    JPanel SlangContainer = new JPanel(new FlowLayout());
    SlangContainer.add(new JLabel("Slang:"));
    ControlSlangField = new JTextField(20);
    SlangContainer.add(ControlSlangField);

    JPanel DefinitionContainer = new JPanel(new FlowLayout());
    DefinitionContainer.add(new JLabel("Definition:"));
    ControlDefinitionField = new JTextField(20);
    DefinitionContainer.add(ControlDefinitionField);

    FieldContainer.add(SlangContainer);
    FieldContainer.add(DefinitionContainer);

    JPanel ButtonContainer = new JPanel(new FlowLayout());
    AddButton = new JButton("Add");
    ButtonContainer.add(AddButton);

    EditButton = new JButton("Edit");
    ButtonContainer.add(EditButton);

    DeleteButton = new JButton("Delete");
    ButtonContainer.add(DeleteButton);

    ControlPane.add(FieldContainer);
    ControlPane.add(ButtonContainer);
  }

  private void prepareLeftPanel() {
    LeftPanel = new JPanel(new BorderLayout());
    prepareSearchUI();
    prepareHistoryUI();
    prepareControlUI();
    LeftPanel.add(SearchPanel, BorderLayout.NORTH);
    LeftPanel.add(ControlPane, BorderLayout.EAST);
    LeftPanel.add(HistoryPane, BorderLayout.WEST);
  }

  public FormPane() {
    setBorder(new EmptyBorder(8, 8, 8, 8));
    setLayout(new BorderLayout());

    prepareLeftPanel();
    add(LeftPanel, BorderLayout.WEST);

    prepareResultUI();
    add(ResultPane, BorderLayout.EAST);

    addEvents();
  }
}
