package controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeMap;
import utils.Constant;

public class SlangManager {

  public class Slang {

    private String slang;
    private ArrayList<String> meaning;

    public Slang(String slang, ArrayList<String> meaning) {
      this.slang = slang;
      this.meaning = meaning;
    }

    public String getSlang() {
      return slang;
    }

    public void setSlang(String slang) {
      this.slang = slang;
    }

    public ArrayList<String> getMeaning() {
      return meaning;
    }

    public void setMeaning(ArrayList<String> meaning) {
      this.meaning = meaning;
    }
  }

  public class Quiz {

    private String question;
    private int answer;
    private ArrayList<String> options;

    public Quiz(String question, int answer, ArrayList<String> options) {
      this.question = question;
      this.answer = answer;
      this.options = options;
    }

    public String getQuestion() {
      return question;
    }

    public void setQuestion(String question) {
      this.question = question;
    }

    public int getAnswer() {
      return answer;
    }

    public void setAnswer(int answer) {
      this.answer = answer;
    }

    public ArrayList<String> getOptions() {
      return options;
    }

    public void setOptions(ArrayList<String> options) {
      this.options = options;
    }

    public String getOption(int index) {
      return options.get(index);
    }
  }

  static TreeMap<String, ArrayList<String>> slangMap = new TreeMap<String, ArrayList<String>>();
  static TreeMap<String, ArrayList<String>> historyMap = new TreeMap<String, ArrayList<String>>();
  static DatasourceManager datasourceManager = new DatasourceManager();

  public ArrayList<String> getMeaning(String slang) {
    return slangMap.get(slang);
  }

  public void setUserSlang() {
    datasourceManager.copyFile(
      Constant.DATASOURCE,
      Constant.USER_SLANG_DATASOURCE
    );
  }

  public TreeMap<String, ArrayList<String>> getAllSlang() {
    return slangMap;
  }

  public TreeMap<String, ArrayList<String>> getHistorySlang() {
    return historyMap;
  }

  public void setSlangList(ArrayList<String> slangList) {
    for (int i = 0; i < slangList.size(); i++) {
      String[] slang = slangList.get(i).split("`");
      if (slang.length == 2) {
        String slangName = slang[0];
        String[] slangMeaningArray = slang[1].split("\\| ");
        ArrayList<String> slangMeaning = new ArrayList<String>();
        for (int j = 0; j < slangMeaningArray.length; j++) {
          slangMeaning.add(slangMeaningArray[j]);
        }
        slangMap.put(slangName, slangMeaning);
      }
    }
  }

  public void loadHistory() {
    ArrayList<String> historyLog = datasourceManager.getData(
      Constant.SLANG_HISTORY_PATH
    );
    if (historyLog != null) {
      for (String log : historyLog) {
        ArrayList<String> history = new ArrayList<String>();
        String slangHistory = log.split(Constant.LOG_SEPARATOR_TIME_CONTENT)[1];
        String[] slangHistoryArray = slangHistory.split(
          Constant.LOG_SEPARATOR_SLANG_DEFINITION
        );
        if (slangHistoryArray.length == 2) {
          String slang = slangHistoryArray[0];
          String[] slangMeaningArray =
            slangHistoryArray[1].split(
                Constant.LOG_SEPARATOR_DEFINITION_DEFINITION
              );
          for (int j = 0; j < slangMeaningArray.length; j++) {
            history.add(slangMeaningArray[j]);
          }
          historyMap.put(slang, history);
        }
      }
    }

    ArrayList<String> historyLogAdd = datasourceManager.getData(
      Constant.MEANING_HISTORY_PATH
    );
    if (historyLogAdd != null) {
      for (String log : historyLogAdd) {
        ArrayList<String> history = new ArrayList<String>();
        String slangHistory = log.split(Constant.LOG_SEPARATOR_TIME_CONTENT)[1];
        String[] slangHistoryArray = slangHistory.split(
          Constant.LOG_SEPARATOR_SLANG_DEFINITION
        );
        if (slangHistoryArray.length == 2) {
          String slang = slangHistoryArray[0];
          String[] slangMeaningArray =
            slangHistoryArray[1].split(
                Constant.LOG_SEPARATOR_DEFINITION_DEFINITION
              );
          for (int j = 0; j < slangMeaningArray.length; j++) {
            history.add(slangMeaningArray[j]);
          }
          historyMap.put(slang, history);
        }
      }
    }
  }

  public void loadSlangs() {
    ArrayList<String> data = new ArrayList<String>();
    if (datasourceManager.checkFileExist(Constant.USER_SLANG_DATASOURCE)) {
      data = datasourceManager.getData(Constant.USER_SLANG_DATASOURCE);
    } else {
      setUserSlang();
      data = datasourceManager.getData(Constant.USER_SLANG_DATASOURCE);
    }

    setSlangList(data);
    loadHistory();
  }

  public void logHistory(Constant.SlangType type, String data) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(
      Constant.TIME_LOG_FORMAT
    );
    LocalDateTime now = LocalDateTime.now();
    String timeLog =
      '[' + dtf.format(now) + ']' + Constant.LOG_SEPARATOR_TIME_CONTENT;

    switch (type) {
      case SLANG:
        String meaning = "";
        if (getMeaning(data) != null) {
          for (String m : getMeaning(data)) {
            if (m == getMeaning(data).get(0)) {
              meaning += m;
            } else {
              meaning += Constant.LOG_SEPARATOR_DEFINITION_DEFINITION + m;
            }
          }
          String historySlang =
            timeLog + data + Constant.LOG_SEPARATOR_SLANG_DEFINITION + meaning;
          datasourceManager.writeData(
            Constant.SLANG_HISTORY_PATH,
            historySlang
          );
        }

        break;
      case MEANING:
        String slang = "";
        for (String key : slangMap.keySet()) {
          if (slangMap.get(key).contains(data)) {
            slang = key;
          }
        }
        if (slang != "") {
          String historyMeaning =
            timeLog + slang + Constant.LOG_SEPARATOR_SLANG_DEFINITION + data;
          datasourceManager.writeData(
            Constant.MEANING_HISTORY_PATH,
            historyMeaning
          );
        }
        break;
      case KEYWORD:
        String historyKeyWord = timeLog + data;
        datasourceManager.writeData(
          Constant.KEYWORD_HISTORY_PATH,
          historyKeyWord
        );
        break;
      default:
        break;
    }
  }

  public TreeMap<String, ArrayList<String>> searchBySlang(String keyword) {
    TreeMap<String, ArrayList<String>> result = new TreeMap<String, ArrayList<String>>();
    for (String slang : slangMap.keySet()) {
      if (slang.contains(keyword)) {
        result.put(slang, slangMap.get(slang));
      }
    }
    logHistory(Constant.SlangType.SLANG, keyword);
    return result;
  }

  public TreeMap<String, ArrayList<String>> searchByDefinition(String keyword) {
    try {
      TreeMap<String, ArrayList<String>> result = new TreeMap<String, ArrayList<String>>();
      for (String slang : slangMap.keySet()) {
        for (String meaning : slangMap.get(slang)) {
          if (meaning.contains(keyword)) {
            result.put(slang, slangMap.get(slang));
          }
        }
      }
      logHistory(Constant.SlangType.MEANING, keyword);

      return result;
    } catch (Exception e) {
      return null;
    }
  }

  public TreeMap<String, ArrayList<String>> searchByAll(String keyword) {
    TreeMap<String, ArrayList<String>> result = new TreeMap<String, ArrayList<String>>();
    TreeMap<String, ArrayList<String>> resultSlang = searchBySlang(keyword);
    TreeMap<String, ArrayList<String>> resultMeaning = searchByDefinition(
      keyword
    );
    if (resultSlang != null) {
      result.putAll(resultSlang);
    }
    if (resultMeaning != null) {
      result.putAll(resultMeaning);
    }
    return result;
  }

  public void showHistory(Constant.SlangType type) {
    if (type == null) {
      return;
    }
    switch (type) {
      case SLANG:
        for (String slang : historyMap.keySet()) {
          System.out.println(slang);
          for (String meaning : historyMap.get(slang)) {
            System.out.println(meaning);
          }
        }
        break;
      case KEYWORD:
        ArrayList<String> keywordLog = datasourceManager.getData(
          Constant.KEYWORD_HISTORY_PATH
        );
        for (String log : keywordLog) {
          System.out.println(log);
        }
        break;
      default:
        break;
    }
  }

  public boolean addDefinition(String slang, String meaning) {
    try {
      ArrayList<String> meaningList = new ArrayList<String>();
      meaningList.add(meaning);
      slangMap.put(slang, meaningList);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean addSlang(
    String slang,
    ArrayList<String> meaning,
    boolean isYes
  ) {
    try {
      if (slangMap.containsKey(slang)) {
        ArrayList<String> oldMeaning = slangMap.get(slang);
        String meaningText = "";

        if (isYes) {
          for (String m : meaning) {
            if (m != meaning.get(meaning.size() - 1)) {
              meaningText += m + "| ";
            } else {
              meaningText += m;
            }
          }
          slangMap.put(slang, meaning);
          datasourceManager.addDefinition(
            Constant.USER_SLANG_DATASOURCE,
            slang,
            meaningText,
            true
          );
        } else {
          for (String m : meaning) {
            oldMeaning.add(m);
            if (m != meaning.get(meaning.size() - 1)) {
              meaningText += m + "| ";
            } else {
              meaningText += m;
            }
          }

          slangMap.put(slang, oldMeaning);
          datasourceManager.addDefinition(
            Constant.USER_SLANG_DATASOURCE,
            slang,
            meaningText,
            false
          );
        }
      } else {
        slangMap.put(slang, meaning);
        String data = slang + "`" + String.join("| ", meaning);
        datasourceManager.writeData(Constant.USER_SLANG_DATASOURCE, data);
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public void removeSlang(String slang) {
    try {
      slangMap.remove(slang);
      datasourceManager.removeLine(Constant.USER_SLANG_DATASOURCE, slang);
    } catch (Exception e) {
      System.out.println("Slang word: " + slang + " does not exist");
    }
  }

  public void resetSlang() {
    try {
      slangMap.clear();
      datasourceManager.clearFile(Constant.USER_SLANG_DATASOURCE);
      setUserSlang();
      ArrayList<String> data = new ArrayList<String>();
      setSlangList(data);
      loadSlangs();
    } catch (Exception e) {
      System.out.println("Error when reset slang");
    }
  }

  public Slang getRandomSlang() {
    Random rand = new Random();
    int randomIndex = rand.nextInt(slangMap.size());
    int i = 0;
    for (String key : slangMap.keySet()) {
      if (i == randomIndex) {
        return new Slang(key, slangMap.get(key));
      }
      i++;
    }
    return null;
  }

  public Quiz quizSlangGame() {
    Slang quizSlang = getRandomSlang();
    ArrayList<String> options = new ArrayList<String>();
    options.add(quizSlang.getMeaning().get(0));
    for (int i = 0; i < 3; i++) {
      Slang randomSlang = getRandomSlang();
      if (!randomSlang.getSlang().equals(quizSlang.getSlang())) {
        options.add(randomSlang.getMeaning().get(0));
      } else {
        i--;
      }
    }

    Collections.shuffle(options);
    int correctAnswer = options.indexOf(quizSlang.getMeaning().get(0));
    Quiz quiz = new Quiz(quizSlang.getSlang(), correctAnswer, options);
    return quiz;
  }

  public Quiz quizDefinitionGame() {
    Slang quizSlang = getRandomSlang();
    ArrayList<String> options = new ArrayList<String>();
    options.add(quizSlang.getSlang());
    for (int i = 0; i < 3; i++) {
      Slang randomSlang = getRandomSlang();
      if (!randomSlang.getSlang().equals(quizSlang.getSlang())) {
        options.add(randomSlang.getSlang());
      } else {
        i--;
      }
    }

    Collections.shuffle(options);
    int correctAnswer = options.indexOf(quizSlang.getSlang());
    Quiz quiz = new Quiz(quizSlang.getMeaning().get(0), correctAnswer, options);

    return quiz;
  }

  public boolean editSlang(String oldSlangName, String newSlangName) {
    // change the slang name
    ArrayList<String> meaning = slangMap.get(oldSlangName);
    try {
      slangMap.remove(oldSlangName);
      slangMap.put(newSlangName, meaning);
      Slang newSlang = new Slang(newSlangName, meaning);
      datasourceManager.updateSlang(
        Constant.USER_SLANG_DATASOURCE,
        oldSlangName,
        newSlang
      );
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public boolean editSlang(String slangName, ArrayList<String> newMeaning) {
    // change the slang meaning
    try {
      slangMap.put(slangName, newMeaning);
      Slang newSlang = new Slang(slangName, newMeaning);
      datasourceManager.updateSlang(
        Constant.USER_SLANG_DATASOURCE,
        slangName,
        newSlang
      );
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public boolean isExistSlang(String slang) {
    return slangMap.containsKey(slang);
  }
}
