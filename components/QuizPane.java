package components;

import controllers.SlangManager;
import controllers.SlangManager.Quiz;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import utils.Constant;

public class QuizPane extends JPanel {

  Constant.QuizGameType quizGameType;
  static SlangManager slangManager = new SlangManager();
  static Quiz quiz;
  JPanel quizPanel;
  JLabel questionLabel;
  JButton answerButton1;
  JButton answerButton2;
  JButton answerButton3;
  JButton answerButton4;

  public void prepareSlangGameUI() {
    quiz = slangManager.quizSlangGame();
    quizPanel = new JPanel(new BorderLayout());

    questionLabel =
      new JLabel("What is the meaning of " + quiz.getQuestion() + "?");
    quizPanel.add(questionLabel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel(new GridLayout(2, 2));
    answerButton1 = new JButton(quiz.getOption(0));
    answerButton2 = new JButton(quiz.getOption(1));
    answerButton3 = new JButton(quiz.getOption(2));
    answerButton4 = new JButton(quiz.getOption(3));

    buttonPanel.add(answerButton1);
    buttonPanel.add(answerButton2);
    buttonPanel.add(answerButton3);
    buttonPanel.add(answerButton4);

    quizPanel.add(buttonPanel, BorderLayout.CENTER);
  }

  public void prepareMeaningGameUI() {
    quiz = slangManager.quizDefinitionGame();
    quizPanel = new JPanel(new BorderLayout());

    questionLabel =
      new JLabel("What is the meaning of " + quiz.getQuestion() + "?");
    quizPanel.add(questionLabel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel(new GridLayout(2, 2));
    answerButton1 = new JButton(quiz.getOption(0));
    answerButton2 = new JButton(quiz.getOption(1));
    answerButton3 = new JButton(quiz.getOption(2));
    answerButton4 = new JButton(quiz.getOption(3));

    buttonPanel.add(answerButton1);
    buttonPanel.add(answerButton2);
    buttonPanel.add(answerButton3);
    buttonPanel.add(answerButton4);

    quizPanel.add(buttonPanel, BorderLayout.CENTER);
  }

  private void renew() {
    if (quizGameType == Constant.QuizGameType.SLANG) {
      quiz = slangManager.quizSlangGame();
      questionLabel.setText(
        "What is the meaning of " + quiz.getQuestion() + "?"
      );
    } else {
      quiz = slangManager.quizDefinitionGame();
      questionLabel.setText("What is slang of " + quiz.getQuestion() + "?");
    }
    answerButton1.setText(quiz.getOption(0));
    answerButton2.setText(quiz.getOption(1));
    answerButton3.setText(quiz.getOption(2));
    answerButton4.setText(quiz.getOption(3));
  }

  private boolean checkAnswer(String answer) {
    int result = quiz.getAnswer();
    String correctAnswer = quiz.getOption(result);
    return answer.equals(correctAnswer);
  }

  public void addEvents() {
    answerButton1.addActionListener(e -> {
      if (checkAnswer(answerButton1.getText())) {
        JOptionPane.showMessageDialog(null, "Correct!");
      } else {
        JOptionPane.showMessageDialog(null, "Wrong!");
      }
      renew();
    });

    answerButton2.addActionListener(e -> {
      if (checkAnswer(answerButton2.getText())) {
        JOptionPane.showMessageDialog(null, "Correct!");
      } else {
        JOptionPane.showMessageDialog(null, "Wrong!");
      }
      renew();
    });

    answerButton3.addActionListener(e -> {
      if (checkAnswer(answerButton3.getText())) {
        JOptionPane.showMessageDialog(null, "Correct!");
      } else {
        JOptionPane.showMessageDialog(null, "Wrong!");
      }
      renew();
    });

    answerButton4.addActionListener(e -> {
      if (checkAnswer(answerButton4.getText())) {
        JOptionPane.showMessageDialog(null, "Correct!");
      } else {
        JOptionPane.showMessageDialog(null, "Wrong!");
      }
      renew();
    });
  }

  public QuizPane(Constant.QuizGameType quizGameType) {
    this.quizGameType = quizGameType;
    if (quizGameType == Constant.QuizGameType.SLANG) {
      prepareSlangGameUI();
    } else {
      prepareMeaningGameUI();
    }
    String title = quizGameType == Constant.QuizGameType.SLANG
      ? "Slang Quiz"
      : "Meaning Quiz";
    setBorder(
      new CompoundBorder(new TitledBorder(title), new EmptyBorder(4, 4, 4, 4))
    );
    add(quizPanel);
    addEvents();
  }
}
