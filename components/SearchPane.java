package components;

import controllers.SlangManager;
import utils.Constant;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class SearchPane extends JPanel {

  static SlangManager slangManager = new SlangManager();

  JComboBox<String> SearchTypeBox;
  JButton SearchButton;
  JTextField SearchField;


  public void prepareUI() {
    SearchTypeBox = new JComboBox<String>(Constant.SEARCH_TYPES);
    SearchButton = new JButton("Search");
    SearchField = new JTextField(20);

    SearchButton.setSize(100, 20);
    SearchField.addActionListener(e -> SearchButton.doClick());

    SearchButton.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          String keyword = SearchField.getText();
          slangManager.searchByKeyword(keyword);
          
        }
      }
    );
  }

  public SearchPane() {
    prepareUI();
    setLayout(new BorderLayout());
    JPanel panel = new JPanel();
    BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
    panel.setLayout(boxLayout);

    // KEYWORD
    JPanel keywordPane = new JPanel(new FlowLayout());
    keywordPane.add(new JLabel("Search for:"), BorderLayout.LINE_START);
    keywordPane.add(SearchField, BorderLayout.LINE_END);

    //SELECT SEARCH TYPE
    JPanel searchTypePane = new JPanel(new FlowLayout());
    searchTypePane.add(new JLabel("By:"));
    searchTypePane.add(SearchTypeBox);

    panel.add(keywordPane, BorderLayout.PAGE_START);
    panel.add(searchTypePane, BorderLayout.CENTER);
    panel.add(SearchButton, BorderLayout.CENTER);
    add(panel);

    setToolTipText("Search for a slang word");

    setBorder(
      new CompoundBorder(
        new TitledBorder("SEARCH"),
        new EmptyBorder(4, 4, 4, 4)
      )
    );
  }
}
