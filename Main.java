import components.FormPane;
import java.awt.*;
import java.com.Constant;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
          JFrame.setDefaultLookAndFeelDecorated(true);
          JFrame frame = new JFrame("Layout");
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          // Define the panel to hold the buttons 
          JPanel panel = new JPanel();
          JButton jb1 = new JButton("NORTH");
          JButton jb2 = new JButton("SOUTH");
          JButton jb3 = new JButton("WEST");
          JButton jb4 = new JButton("EAST");
          JButton jb5 = new JButton("CENTER");
          // Define the panel to hold the buttons
          panel.setLayout(new BorderLayout());
          panel.add(jb1, BorderLayout.NORTH);
          panel.add(jb2, BorderLayout.SOUTH);
          panel.add(jb3, BorderLayout.WEST);
          panel.add(jb4, BorderLayout.EAST);
          panel.add(jb5, BorderLayout.CENTER);
          // Set the window to be visible as the default to be false
          frame.add(panel);
          frame.pack();
          frame.setVisible(true);
        }
      }
    );
  }
}
