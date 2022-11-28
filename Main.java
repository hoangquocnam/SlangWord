import components.FormPane;
import controllers.SlangManager;
import controllers.SlangManager.Slang;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import utils.Constant;

public class Main {

  static SlangManager slangManager = new SlangManager();

  public static void main(String[] args) {
    // new Main();
    slangManager.loadSlangs();

    // slangManager.searchByKeyword("God");

    // slangManager.searchBySlang("WHEW");
    // slangManager.searchBySlang("ABU");
  
    // ArrayList<String> meaning = new ArrayList<String>();
    // meaning.add("Alpha Beta Unlimited");


    // slangManager.removeSlang("ABU");

    // slangManager.resetSlang();

    Slang slang = slangManager.getRandomSlang();

    System.out.println(slang.getSlang() + " " + slang.getMeaning());

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
