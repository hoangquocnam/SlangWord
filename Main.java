import components.FormPane;
import utils.Constant;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
          frame.add(new FormPane(), BorderLayout.PAGE_START);
          frame.pack();
          frame.setLocationRelativeTo(null);
          frame.setVisible(true);
        }
      }
    );
  }
}
