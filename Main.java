import components.SearchPane;
import java.awt.*;
import java.com.Constant;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class Main {

  public static void main(String[] args) {
    new Main();
  }

  public Main() {
    EventQueue.invokeLater(
      new Runnable() {
        @Override
        public void run() {
          try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          } catch (ClassNotFoundException ex) {} catch (
            InstantiationException ex
          ) {} catch (IllegalAccessException ex) {} catch (
            UnsupportedLookAndFeelException ex
          ) {}
          JFrame frame = new JFrame(Constant.APP_NAME);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setSize(Constant.APP_WIDTH, Constant.APP_HEIGHT);
          frame.setLayout(new BorderLayout());
          frame.add(new FormPane());
          frame.pack();
          frame.setLocationRelativeTo(null);
          frame.setVisible(true);
        }
      }
    );
  }

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
      searchPane.setBorder(
        new CompoundBorder(
          new TitledBorder("Search"),
          new EmptyBorder(4, 4, 4, 4)
        )
      );
      add(searchPane, gbc);
    }
  }
}
